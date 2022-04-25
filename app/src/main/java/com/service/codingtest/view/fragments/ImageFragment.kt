package com.service.codingtest.view.fragments

import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import androidx.paging.LoadState
import androidx.savedstate.SavedStateRegistryOwner
import com.service.codingtest.R
import com.service.codingtest.databinding.FragImageBinding
import com.service.codingtest.db.AppDB
import com.service.codingtest.network.ImageAPI
import com.service.codingtest.repository.DbImagePostRepository
import com.service.codingtest.view.adapters.ImageAdapter
import com.service.codingtest.view.adapters.ImageLoadStateAdapter
import com.service.codingtest.viewmodel.ImageListViewModel
//import com.service.codingtest.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.frag_image.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter


class ImageFragment : Fragment() {

    private lateinit var binding: FragImageBinding

    private lateinit var adapter: ImageAdapter

//    private val model: SharedViewModel by activityViewModels()

    class MainViewModelFactory(owner: SavedStateRegistryOwner, private val documentRepository :DbImagePostRepository) : AbstractSavedStateViewModelFactory(owner, null) {
        override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
            @Suppress("UNCHECKED_CAST")
            return ImageListViewModel(handle, documentRepository) as T
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_image, container, false)
        binding = FragImageBinding.bind(view)
        binding.vm = ViewModelProvider(requireActivity(), MainViewModelFactory(this, DbImagePostRepository(
            AppDB.getInstance(requireContext().applicationContext as Application), ImageAPI.create())))
            .get(ImageListViewModel::class.java)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initImageListView()
        initSwipeToRefresh()
        initSearchEditText()
    }

    private fun initImageListView() {
        adapter = ImageAdapter()
        rv_image.adapter = adapter.withLoadStateHeaderAndFooter(
            header = ImageLoadStateAdapter(adapter),
            footer = ImageLoadStateAdapter(adapter)
        )

//        model.text.observe(viewLifecycleOwner, {
//            adapter.notifyDataSetChanged()
//        })

        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            binding.vm!!.posts.collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            adapter.loadStateFlow.collectLatest { loadStates ->
                layout_swipe_refresh.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launchWhenCreated {
            @OptIn(FlowPreview::class)
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { rv_image.scrollToPosition(0) }
        }
    }

    private fun initSwipeToRefresh() {
        layout_swipe_refresh.setOnRefreshListener { adapter.refresh() }
    }

    private fun initSearchEditText() =
        et_search.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                binding.vm!!.showSubreddit()
                return@OnKeyListener true
            }
            false
        })

}
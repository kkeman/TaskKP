package com.service.codingtest.view.fragments


import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.savedstate.SavedStateRegistryOwner
import com.service.codingtest.R
import com.service.codingtest.databinding.FragImageBinding
import com.service.codingtest.db.AppDB
import com.service.codingtest.network.ImageAPI
import com.service.codingtest.repository.DbImagePostRepository
import com.service.codingtest.view.adapters.ImageAdapter
import com.service.codingtest.view.adapters.ImageLoadStateAdapter
import com.service.codingtest.viewmodel.ImageListViewModel
import com.service.codingtest.viewmodel.SharedViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.frag_image.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import java.util.concurrent.TimeUnit


class ImageFragment : Fragment() {

    private lateinit var binding: FragImageBinding

    private val adapter: ImageAdapter by lazy { ImageAdapter(requireContext()) }

    private val viewModel: SharedViewModel by activityViewModels()

    class MainViewModelFactory(
        owner: SavedStateRegistryOwner,
        private val documentRepository: DbImagePostRepository
    ) : AbstractSavedStateViewModelFactory(owner, null) {
        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T {
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
        binding.vm = ViewModelProvider(
            requireActivity(), MainViewModelFactory(
                this, DbImagePostRepository(
                    AppDB.getInstance(requireContext().applicationContext as Application),
                    ImageAPI.create()
                )
            )
        )
            .get(ImageListViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initImageListView()
        initSwipeToRefresh()
        initSearchEditText()

        viewModel.getSelected().observe(viewLifecycleOwner, Observer { result ->
            adapter.notifyItemChanged(result)
        })
    }

    private fun initImageListView() {
        rv_image.setOnTouchListener { view, event ->
            if( event.action == MotionEvent.ACTION_UP ) {
                view.performClick();
            }
            val imm: InputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        rv_image.adapter = adapter.withLoadStateHeaderAndFooter(
            header = ImageLoadStateAdapter(adapter),
            footer = ImageLoadStateAdapter(adapter)
        )

        lifecycleScope.launchWhenCreated {
            binding.vm!!.posts.collectLatest {
                adapter.addLoadStateListener { loadState ->
                    binding.apply {
                        if(loadState.mediator!!.refresh is LoadState.Error) {
                            val error = when {
                                loadState.mediator?.prepend is LoadState.Error -> loadState.mediator?.prepend as LoadState.Error
                                loadState.mediator?.append is LoadState.Error -> loadState.mediator?.append as LoadState.Error
                                loadState.mediator?.refresh is LoadState.Error -> loadState.mediator?.refresh as LoadState.Error

                                else -> null
                            }
                            error?.let {
                                if (adapter.snapshot().isEmpty()) {
                                    tvError.isVisible = true
                                    tvError.text = it.error.localizedMessage
                                }
                            }

                        } else if(loadState.source.refresh is LoadState.NotLoading
                            && loadState.append.endOfPaginationReached
                            && adapter.itemCount < 1) {
                            tvError.isVisible = true
                            tvError.text = getString(R.string.search_empty)

                        } else{
                            tvError.isVisible = false
                        }
                    }
                }

                adapter.submitData(it)

                adapter.loadStateFlow.collectLatest { loadStates ->
                    layout_swipe_refresh.isRefreshing = loadStates.refresh is LoadState.Loading
                }

                adapter.loadStateFlow
                    .distinctUntilChangedBy { it.refresh }
                    .filter { it.refresh is LoadState.NotLoading }
                    .collect { rv_image.scrollToPosition(0) }
            }
        }
    }

    private fun initSwipeToRefresh() {
        layout_swipe_refresh.setOnRefreshListener { adapter.refresh() }
    }

    private fun initSearchEditText() {
        val observableTextQuery =
            Observable.create(ObservableOnSubscribe { emitter: ObservableEmitter<String>? ->
                et_search.addTextChangedListener(object : TextWatcher {
                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {}
                    override fun beforeTextChanged(
                        s: CharSequence?, start: Int,
                        count: Int, after: Int
                    ) {
                        emitter?.onNext(s.toString())
                    }
                })
            })
                .debounce(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        observableTextQuery.subscribe(object : Observer<String> {
            override fun onComplete() {}
            override fun onSubscribe(d: io.reactivex.rxjava3.disposables.Disposable?) {}
            override fun onNext(t: String) {
                binding.vm!!.showSubreddit()
            }

            override fun onError(e: Throwable?) {}
        })

    }

}
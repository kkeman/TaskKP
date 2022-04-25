package com.service.codingtest.view.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.service.codingtest.R
import com.service.codingtest.databinding.FragFavoriteBinding
import com.service.codingtest.db.AppDB
import com.service.codingtest.network.MLog
import com.service.codingtest.view.adapters.FavoriteAdapter
import com.service.codingtest.viewmodel.FavoriteViewModel

class FavoriteFragment : Fragment() {

    private val mTAG = FavoriteFragment::class.java.name

    private var mMediaFileAdapter: FavoriteAdapter? = null

    private lateinit var viewDataBinding: FragFavoriteBinding
    private val viewModel by viewModels<FavoriteViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = FragFavoriteBinding.bind(inflater.inflate(R.layout.frag_favorite, container, false)).apply { viewmodel = viewModel  }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
        setList()
        initSwipeRefresh()
    }

    private fun setList() {
        setRecyclerViewLayoutManager()

        setMediaDBList()
    }

    fun setMediaDBList() {
        viewModel.list = AppDB.getInstance(requireContext()).favoriteDao().loadAll()

        viewModel.list.observe(viewLifecycleOwner, {
            mMediaFileAdapter = FavoriteAdapter(it)
            viewDataBinding.rvMediaFileList.adapter = mMediaFileAdapter
        })
    }

    private fun setRecyclerViewLayoutManager() {
        var scrollPosition = 0

        if (viewDataBinding.rvMediaFileList!!.layoutManager != null) {
            scrollPosition = (viewDataBinding.rvMediaFileList!!.layoutManager as LinearLayoutManager)
                    .findFirstCompletelyVisibleItemPosition()
        }

        val llm = LinearLayoutManager(activity)
        viewDataBinding.rvMediaFileList!!.layoutManager = llm
        val dividerItemDecoration = DividerItemDecoration(activity, llm.orientation)
        viewDataBinding.rvMediaFileList.addItemDecoration(dividerItemDecoration)

        viewDataBinding.rvMediaFileList!!.scrollToPosition(scrollPosition)
    }

    private fun initSwipeRefresh() {
        viewDataBinding.layoutSwipeRefresh.setColorSchemeColors(Color.parseColor("#58be17"))
        viewDataBinding.layoutSwipeRefresh.setOnRefreshListener {
            viewDataBinding.layoutSwipeRefresh.isRefreshing = false
            setMediaDBList()
        }
    }
}
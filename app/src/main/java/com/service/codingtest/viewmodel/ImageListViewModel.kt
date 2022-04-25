package com.service.codingtest.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.paging.PagingData
import com.service.codingtest.repository.DbImagePostRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class ImageListViewModel(private val savedStateHandle: SavedStateHandle, private val documentRepository :DbImagePostRepository) : ViewModel() {

    companion object {
        const val KEY_SEARCH = "search"
    }

    val searchWord = ObservableField("target")

    private val clearListCh = Channel<Unit>(Channel.CONFLATED)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val posts = flowOf(
        clearListCh.consumeAsFlow().map {

            PagingData.empty()
        },
        savedStateHandle.getLiveData<String>(KEY_SEARCH)
            .asFlow()
            .flatMapLatest {
                documentRepository.postsOfSubDocument(it, 30)
            }
    ).flattenMerge(2)

    fun showSubreddit() {
        clearListCh.offer(Unit)

        savedStateHandle.set(KEY_SEARCH, searchWord.get().toString())
    }
}
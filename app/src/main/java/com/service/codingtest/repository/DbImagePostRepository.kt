package com.service.codingtest.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.service.codingtest.db.AppDB
import com.service.codingtest.network.ImageAPI

class DbImagePostRepository(val db: AppDB, private val imageAPI: ImageAPI) : ImageRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun postsOfSubDocument(
        query: String,
        pageSize: Int
    ) = Pager(
        config = PagingConfig(pageSize = pageSize),
        remoteMediator = PageKeyedRemoteMediator(db, imageAPI, query)) {

        db.imageDao().loadAll(query)
    }.flow
}

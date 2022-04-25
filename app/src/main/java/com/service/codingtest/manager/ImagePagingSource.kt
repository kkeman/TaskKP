package com.service.codingtest.manager

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult.Page
import androidx.paging.PagingState
import com.service.codingtest.model.response.ItemsEntity
import com.service.codingtest.network.ImageAPI
import retrofit2.HttpException
import java.io.IOException

 /* unused */
class ImagePagingSource(
    private val httpClient: ImageAPI,
    private val query: String,
) : PagingSource<Int, ItemsEntity>() {

    private val initialPageIndex: Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemsEntity> {
        val position = params.key ?: initialPageIndex
        return try {
            val items = httpClient.getAPI(query = query, page = position).items

            Page(
                data = items,
                prevKey = if (position == initialPageIndex) null else position - 1,
                nextKey = if (items.isEmpty()) null else position + 1
            )

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

     override fun getRefreshKey(state: PagingState<Int, ItemsEntity>): Int? {
         TODO("Not yet implemented")
     }
 }
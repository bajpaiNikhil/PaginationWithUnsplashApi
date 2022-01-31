package com.example.paginationwithunsplashapi.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.paginationwithunsplashapi.Utils.Constants.ITEMS_PER_PAGE
import com.example.paginationwithunsplashapi.data.local.UnsplashDatabase
import com.example.paginationwithunsplashapi.data.paging.UnsplashRemoteMediator
import com.example.paginationwithunsplashapi.data.remote.UnsplashApi
import com.example.paginationwithunsplashapi.model.UnsplashImage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject





@ExperimentalPagingApi
class Repository @Inject constructor(
    private val unsplashApi: UnsplashApi,
    private val unsplashDatabase: UnsplashDatabase
) {


    fun getAllImages() : Flow<PagingData<UnsplashImage>> {
        val pagingSourceFactory = { unsplashDatabase.unsplashImageDao().getAllImages()}
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            remoteMediator = UnsplashRemoteMediator(
                unsplashApi = unsplashApi,
                unsplashDatabase = unsplashDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}
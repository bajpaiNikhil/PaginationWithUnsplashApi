package com.example.paginationwithunsplashapi.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.paginationwithunsplashapi.data.local.UnsplashDatabase
import com.example.paginationwithunsplashapi.data.remote.UnsplashApi
import com.example.paginationwithunsplashapi.model.UnsplashImage
import com.example.paginationwithunsplashapi.model.UnsplashRemoteKeys
import java.lang.Exception
import javax.inject.Inject

@ExperimentalPagingApi
class UnsplashRemoteMediator @Inject constructor(
    private val unsplashApi: UnsplashApi,
    private val unsplashDatabase: UnsplashDatabase
):RemoteMediator<Int , UnsplashImage>() {

    private val unsplashImageDao =  unsplashDatabase.unsplashImageDao()
    private val unspashRemotekeysDao = unsplashDatabase.unsplashRemoteKeysDao()



    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UnsplashImage>
    ): MediatorResult {
       return try {
           val currentPage = when (loadType){
               LoadType.REFRESH ->{
                   val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                   remoteKeys?.nextPage?.minus( 1)?:1
               }
               LoadType.PREPEND ->{
                   val remoteKeys = getRemoteKeyForFirstItem(state)
                   val prevPage = remoteKeys?.prevPage
                       ?: return MediatorResult.Success(
                       endOfPaginationReached = remoteKeys !=null
                   )
                   prevPage
               }
               LoadType.APPEND ->{
                   val remoteKeys = getRemoteKeyForLastItem(state)
                   val nextPage = remoteKeys?.nextPage
                       ?: return MediatorResult.Success(
                           endOfPaginationReached = remoteKeys!=null
                       )
                   nextPage
               }
           }

           val response = unsplashApi.getAllImage(page = currentPage , per_page = 10)
           val endOfPaginationReached = response.isEmpty()

           val prevPage = if (currentPage == 1) null else currentPage-1
           val nextPage = if (endOfPaginationReached) null else currentPage + 1

           unsplashDatabase.withTransaction {
               if (loadType == LoadType.REFRESH) {
                   unsplashImageDao.deleteAllImages()
                   unspashRemotekeysDao.deleteAllRemoteKeys()
               }
               val keys = response.map { unsplashImage ->
                   UnsplashRemoteKeys(
                       id = unsplashImage.id,
                       prevPage = prevPage,
                       nextPage = nextPage
                   )
               }
               unspashRemotekeysDao.addAllRemoteKeys(remoteKeys = keys)
               unsplashImageDao.addImage(image = response)
           }
           MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
       }catch (e: Exception){
           return MediatorResult.Error(e)
       }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, UnsplashImage>
    ): UnsplashRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                unspashRemotekeysDao.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, UnsplashImage>
    ): UnsplashRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { unsplashImage ->
                unspashRemotekeysDao.getRemoteKeys(id = unsplashImage.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, UnsplashImage>
    ): UnsplashRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { unsplashImage ->
                unspashRemotekeysDao.getRemoteKeys(id = unsplashImage.id)
            }
    }
}
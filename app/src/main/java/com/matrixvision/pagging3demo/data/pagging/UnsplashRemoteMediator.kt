package com.matrixvision.pagging3demo.data.pagging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.matrixvision.pagging3demo.data.local.UnsplashDatabase
import com.matrixvision.pagging3demo.data.local.dao.UnsplashImageDao
import com.matrixvision.pagging3demo.data.remote.UnsplashApi
import com.matrixvision.pagging3demo.model.UnsplashImage
import com.matrixvision.pagging3demo.model.UnsplashRemoteKey
import com.matrixvision.pagging3demo.util.Constants.ITEMS_PER_PAGE
import javax.inject.Inject

@ExperimentalPagingApi
class UnsplashRemoteMediator @Inject constructor(
    private val unsplashApi: UnsplashApi,
    private val unsplashDatabase: UnsplashDatabase
):RemoteMediator<Int, UnsplashImage>() {
    private val unsplashImageDao = unsplashDatabase.unsplashImageDao()
    private val unsplashRemoteKeyDao = unsplashDatabase.unsplashRemoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UnsplashImage>
    ): MediatorResult {

            return try{

                val currentPage = when (loadType) {
                    LoadType.REFRESH -> {
                        val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                        remoteKeys?.nextPage?.minus(1) ?: 1
                    }
                    LoadType.PREPEND -> {
                        val remoteKeys = getRemoteKeyForFirstItem(state)
                        val prevPage = remoteKeys?.prevPage
                            ?: return MediatorResult.Success(
                                endOfPaginationReached = remoteKeys != null
                            )
                        prevPage
                    }
                    LoadType.APPEND -> {
                        val remoteKeys = getRemoteKeyForLastItem(state)
                        val nextPage = remoteKeys?.nextPage
                            ?: return MediatorResult.Success(
                                endOfPaginationReached = remoteKeys != null
                            )
                        nextPage
                    }
                }

                val response = unsplashApi.getAllImages(page = currentPage, perPage = ITEMS_PER_PAGE)
                val endOfPaginationReached = response.isEmpty()

                val prevPage = if (currentPage == 1) null else currentPage - 1
                val nextPage = if (endOfPaginationReached) null else currentPage + 1

                unsplashDatabase.withTransaction {
                    if (loadType == LoadType.REFRESH){
                        unsplashImageDao.deleteImages()
                        unsplashRemoteKeyDao.deleteAllRemoteKeys()
                    }
                    val keys = response.map { unsplashImage ->
                        UnsplashRemoteKey(
                            id = unsplashImage.id,
                            prevPage = prevPage,
                            nextPage = nextPage
                        )
                    }
                    unsplashRemoteKeyDao.addAllRemoteKeys(remoteKey = keys)
                    unsplashImageDao.addImages(images = response)
                }
                MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

            }catch (e:Exception){
                return MediatorResult.Error(e)
            }

    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, UnsplashImage>
    ): UnsplashRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                unsplashRemoteKeyDao.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, UnsplashImage>
    ): UnsplashRemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { unsplashImage ->
                unsplashRemoteKeyDao.getRemoteKeys(id = unsplashImage.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, UnsplashImage>
    ): UnsplashRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { unsplashImage ->
                unsplashRemoteKeyDao.getRemoteKeys(id = unsplashImage.id)
            }
    }




}
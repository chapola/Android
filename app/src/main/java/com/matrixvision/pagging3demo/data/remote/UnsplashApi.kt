package com.matrixvision.pagging3demo.data.remote

import com.matrixvision.pagging3demo.BuildConfig
import com.matrixvision.pagging3demo.model.SearchResult
import com.matrixvision.pagging3demo.model.UnsplashImage
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface UnsplashApi {

    @Headers("Authorization: Client-ID ${BuildConfig.API_KEY}")
    @GET("/photos")
    suspend fun getAllImages(
         @Query("page")
         page:Int,
         @Query("per_page")
         perPage:Int

    ):List<UnsplashImage>

    @Headers("Authorization: Client-ID ${BuildConfig.API_KEY}")
    @GET("/search/photos")
    suspend fun searchImages(
        @Query("query")
        query:String,
        @Query("per_page")
        perPage:Int

    ):SearchResult
}
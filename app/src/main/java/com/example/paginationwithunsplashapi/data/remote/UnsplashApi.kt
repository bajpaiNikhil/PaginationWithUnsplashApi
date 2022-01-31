package com.example.paginationwithunsplashapi.data.remote

import com.example.paginationwithunsplashapi.BuildConfig
import com.example.paginationwithunsplashapi.model.UnsplashImage
import retrofit2.http.GET

import retrofit2.http.Headers
import retrofit2.http.Query


interface UnsplashApi {

    @Headers("Authorization Client-ID ${BuildConfig.API_KEY}")
    @GET("/photos")
    suspend fun getAllImage(
        @Query("page") page:Int,
        @Query("per_page") per_page :Int
    ) : List<UnsplashImage>

    @Headers("Authorization Client-ID ${BuildConfig.API_KEY}")
    @GET("/search/photos")
    suspend fun searchImages(
        @Query("page") page:Int,
        @Query("per_page") per_page :Int
    ) : List<UnsplashImage>
}
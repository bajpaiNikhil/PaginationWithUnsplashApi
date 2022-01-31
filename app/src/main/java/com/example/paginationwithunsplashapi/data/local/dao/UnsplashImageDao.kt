package com.example.paginationwithunsplashapi.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.paginationwithunsplashapi.model.UnsplashImage


@Dao
interface UnsplashImageDao {


    @Query("SELECT * FROM unsplash_image_table")
    fun getAllImages() : PagingSource<Int , UnsplashImage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImage(image : List<UnsplashImage>)

    @Query("DELETE FROM unsplash_image_table")
    suspend fun deleteAllImages()




}
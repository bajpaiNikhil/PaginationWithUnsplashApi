package com.example.paginationwithunsplashapi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.paginationwithunsplashapi.data.local.dao.UnsplashImageDao
import com.example.paginationwithunsplashapi.data.local.dao.UnsplashRemoteKeyDao
import com.example.paginationwithunsplashapi.model.UnsplashImage
import com.example.paginationwithunsplashapi.model.UnsplashRemoteKeys


@Database(entities = [UnsplashImage::class ,  UnsplashRemoteKeys::class] , version = 1 )
abstract class UnsplashDatabase:RoomDatabase() {

    abstract fun unsplashImageDao() : UnsplashImageDao
    abstract fun unsplashRemoteKeysDao() : UnsplashRemoteKeyDao


}
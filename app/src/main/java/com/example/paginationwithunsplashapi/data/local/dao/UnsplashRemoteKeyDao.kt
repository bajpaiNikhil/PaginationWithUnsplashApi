package com.example.paginationwithunsplashapi.data.local.dao

import androidx.room.*
import com.example.paginationwithunsplashapi.model.UnsplashRemoteKeys


@Dao
interface UnsplashRemoteKeyDao {

    @Query("SELECT * FROM unsplash_remote_keys_table WHERE id =:id")
    suspend fun getRemoteKeys(id:String) : UnsplashRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<UnsplashRemoteKeys>)

    @Query( "DELETE FROM unsplash_remote_keys_table")
    suspend fun deleteAllRemoteKeys()



}
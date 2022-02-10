package com.matrixvision.pagging3demo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.matrixvision.pagging3demo.model.UnsplashRemoteKey

@Dao
interface UnsplashRemoteKeyDao {

    @Query("SELECT * FROM unsplash_remote_key_table where id=:id")
    suspend fun getRemoteKeys(id:String):UnsplashRemoteKey

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKey: List<UnsplashRemoteKey>)

    @Query("DELETE FROM unsplash_remote_key_table")
    suspend fun deleteAllRemoteKeys()

}
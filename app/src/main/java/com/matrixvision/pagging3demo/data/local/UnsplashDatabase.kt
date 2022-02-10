package com.matrixvision.pagging3demo.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.matrixvision.pagging3demo.data.local.dao.UnsplashImageDao
import com.matrixvision.pagging3demo.data.local.dao.UnsplashRemoteKeyDao
import com.matrixvision.pagging3demo.model.UnsplashImage
import com.matrixvision.pagging3demo.model.UnsplashRemoteKey

@Database(entities = [UnsplashImage::class,UnsplashRemoteKey::class], version = 1)
abstract class UnsplashDatabase:RoomDatabase() {

    abstract fun unsplashImageDao():UnsplashImageDao

    abstract fun unsplashRemoteKeyDao():UnsplashRemoteKeyDao
}
package com.matrixvision.pagging3demo.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.matrixvision.pagging3demo.util.Constants

@Entity(tableName = Constants.UNSPLASH_REMOTE_KEY_TABLE)
data class UnsplashRemoteKey(
    @PrimaryKey
    val id:String,
    val prevPage:Int?,
    val nextPage:Int?

)

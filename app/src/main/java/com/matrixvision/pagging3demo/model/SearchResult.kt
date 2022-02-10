package com.matrixvision.pagging3demo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    @SerialName("results")
    val images:List<UnsplashImage>
)

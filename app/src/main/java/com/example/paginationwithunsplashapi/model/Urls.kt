package com.example.paginationwithunsplashapi.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Urls(
    val regular : String
//    @SerializedName("regular") // to rename the variable name
//    val regularImage : String

)

package com.sesac.angam.data.model.response

import com.google.gson.annotations.SerializedName

data class RatingResponse(
    @SerializedName("brand")
    val brand: String,
    @SerializedName("history")
    val history: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("keywordList")
    val keywordList: List<String>,
    @SerializedName("liked")
    val liked: Boolean,
    @SerializedName("postId")
    val postId: Int,
    @SerializedName("purchasePrice")
    val purchasePrice: Int,
    @SerializedName("size")
    val size: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("wearNum")
    val wearNum: String
)
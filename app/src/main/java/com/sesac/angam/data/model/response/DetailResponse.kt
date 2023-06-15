package com.sesac.angam.data.model.response

data class DetailResponse(
    val brand: String,
    val history: String,
    val postImage: String,
    val keywordList: List<String>,
    val liked: Boolean,
    val postId: Int,
    val purchasePrice: Int,
    val size: String,
    val title: String,
    val wearNum: String,
    val userName: String,
    val userImage: String,
    val isSeller: Boolean,
)
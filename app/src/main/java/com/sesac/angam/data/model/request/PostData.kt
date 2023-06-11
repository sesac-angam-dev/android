package com.sesac.angam.data.model.request

data class PostData(
    val postList: List<PostItem>
)

data class PostItem(
    val brand: String,
    val history: String,
    val image: String,
    val keywords: List<String>,
    val purchasePrice: Int,
    val size: String,
    val title: String,
    val wearNum: Int
)
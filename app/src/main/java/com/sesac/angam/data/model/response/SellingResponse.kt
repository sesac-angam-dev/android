package com.sesac.angam.data.model.response

data class SellingResponse(
    val brand: String,
    val highestBid: Int,
    val image: String,
    val liked: Boolean,
    val postId: Int,
    val title: String
)
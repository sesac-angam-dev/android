package com.sesac.angam.data.model.response

data class RecommendResponse(
    val brand: String,
    val highestBid: Int,
    val id: Int,
    val image: String,
    val liked: Boolean,
    val title: String
)
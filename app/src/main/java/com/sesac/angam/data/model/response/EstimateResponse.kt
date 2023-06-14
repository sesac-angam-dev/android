package com.sesac.angam.data.model.response

data class EstimateResponse(
    val meanBid: Int,
    val postId: Int,
    val title: String,
    val brand: String,
    val userBidInfoList: List<UserBidInfo>
) {
    data class UserBidInfo(
        val bid: Int,
        val image: String,
        val name: String,
        val userId: Int
    )
}
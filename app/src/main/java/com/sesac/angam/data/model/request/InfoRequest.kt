package com.sesac.angam.data.model.request

data class InfoRequest(
    val brand: String,
    val history: String,
    val images: String,
    val keywords: List<String>,
    val purchasePrice: Int,
    val size: String,
    val title: String,
    val wearNum: Int
)

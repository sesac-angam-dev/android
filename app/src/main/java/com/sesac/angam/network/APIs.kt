package com.sesac.angam.network



import com.sesac.angam.data.model.request.PostData
import retrofit2.Response
import retrofit2.http.*

interface APIs {

//    @GET("/api/home")
//    suspend fun homeCategory(
//        @Header("Authorization") accessToken: String,
//        @Query("category") category: String,
//        @Query("latitude") latitude: Double,
//        @Query("longitude") longitude: Double
//    ): Response<CategoryResponse>
//
//
//    @POST("/api/restaurant/review")
//    suspend fun reviewPost(
//        @Header("Authorization") accessToken: String,
//        @Body request: PostReviewRequest
//    )

    @POST("/posts/info")
    suspend fun infoPost(
        @Header("Authorization") accessToken: String,
        @Body request: PostData
    )

}
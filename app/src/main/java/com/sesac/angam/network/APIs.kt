package com.sesac.angam.network



import com.sesac.angam.data.model.request.PostData
import com.sesac.angam.data.model.response.RatingResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface APIs {

    @GET("/posts/estimating")
    suspend fun getRating(
        @Header("Authorization") accessToken: String
    ): Response<List<RatingResponse>>




//    @POST("/api/restaurant/review")
//    suspend fun reviewPost(
//        @Header("Authorization") accessToken: String,
//        @Body request: PostReviewRequest
//    )

    @Multipart
    @POST("/posts/info")
    suspend fun infoPost(
        @Header("Authorization") accessToken: String,
        @PartMap fields: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: List<MultipartBody.Part?>
    )


}
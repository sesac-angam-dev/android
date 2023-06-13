package com.sesac.angam.network



import com.sesac.angam.data.model.request.PostData
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @Multipart
    @POST("/posts/info")
    suspend fun infoPost(
        @Header("Authorization") accessToken: String,
        @PartMap fields: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: List<MultipartBody.Part?>
    )
}
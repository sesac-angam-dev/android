package com.sesac.angam.network



import com.sesac.angam.data.model.request.PostData
import com.sesac.angam.data.model.request.PostPickup
import com.sesac.angam.data.model.response.EstimateResponse
import com.sesac.angam.data.model.response.HotResponse
import com.sesac.angam.data.model.response.RatingResponse
import com.sesac.angam.data.model.response.RecommendResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface APIs {

    @GET("/posts/estimating")
    suspend fun getRating(
        @Header("Authorization") accessToken: String
    ): Response<List<RatingResponse>>

    @GET("/estimated-bid/list")
    suspend fun getEstimate(
        @Header("Authorization") accessToken: String
    ): Response<List<EstimateResponse>>

    @POST("/posts/pickup")
    suspend fun postPickup(
        @Header("Authorization") accessToken: String,
        @Body postPickup: PostPickup
    )

    @GET("/home/posts/hot")
    suspend fun getHot(
        @Header("Authorization") accessToken: String
    ): Response<List<HotResponse>>

    @GET("/home/posts/recommended")
    suspend fun getRecommend(
        @Header("Authorization") accessToken: String
    ): Response<List<RecommendResponse>>


    @Multipart
    @POST("/posts/info")
    suspend fun infoPost(
        @Header("Authorization") accessToken: String,
        @PartMap fields: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: List<MultipartBody.Part?>
    )


}
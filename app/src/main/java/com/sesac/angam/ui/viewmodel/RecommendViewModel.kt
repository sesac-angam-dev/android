package com.sesac.angam.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sesac.angam.GlobalApplication
import com.sesac.angam.data.model.response.HotResponse
import com.sesac.angam.data.model.response.RatingResponse
import com.sesac.angam.data.model.response.RecommendResponse
import com.sesac.angam.network.APIs
import com.sesac.angam.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class RecommendViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var retService: APIs
    private val _tasks = MutableLiveData<List<RecommendResponse>>()
    val tasks: LiveData<List<RecommendResponse>> = _tasks
    private var accessToken = GlobalApplication.prefs.getString("userAccessToken", "")

    init {
        fetchTasks()
    }

    private fun fetchTasks() {
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIs::class.java)

        viewModelScope.launch {
            try {
                val response = retService.getRecommend("Bearer $accessToken")
                if (response.isSuccessful) {
                    _tasks.value = response.body()
                    Log.d("RecommendViewModel API Success", "fetchTasks: ${response.body()}")
                } else {
                    Log.d("RecommendViewModel API Fail1", "fetchTasks: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.d("v API Fail2", "fetchTasks: ${e.message}")
            }
        }
    }
}

package com.sesac.angam.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sesac.angam.GlobalApplication
import com.sesac.angam.data.model.response.BidListResponse
import com.sesac.angam.network.APIs
import com.sesac.angam.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class BiddingViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var retService: APIs
    private val _tasks = MutableLiveData<List<BidListResponse>>()
    val tasks: LiveData<List<BidListResponse>> = _tasks
    private var accessToken = GlobalApplication.prefs.getString("userAccessToken", "")
    private var postId = GlobalApplication.prefs.getString("postId", "").toLong()

    init {
        fetchTasks()
    }

    private fun fetchTasks() {
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIs::class.java)

        viewModelScope.launch {
            try {
                val response = retService.getBidList("Bearer $accessToken",postId)
                if (response.isSuccessful) {
                    _tasks.value = response.body()
                    Log.d("BiddingViewModel API Success", "fetchTasks: ${response.body()}")
                } else {
                    Log.d("BiddingViewModel API Fail1", "fetchTasks: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.d("BiddingViewModel API Fail2", "fetchTasks: ${e.message}")
            }
        }
    }
}

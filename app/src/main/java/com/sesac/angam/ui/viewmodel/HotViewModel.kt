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
import com.sesac.angam.network.APIs
import com.sesac.angam.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class HotViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var retService: APIs
    private val _tasks = MutableLiveData<List<HotResponse>>()
    val tasks: LiveData<List<HotResponse>> = _tasks
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
                val response = retService.getHot("Bearer $accessToken")
                if (response.isSuccessful) {
                    _tasks.value = response.body()
                    Log.d("HotViewModel API Success", "fetchTasks: ${response.body()}")
                } else {
                    Log.d("HotViewModel API Fail1", "fetchTasks: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.d("HotViewModel API Fail2", "fetchTasks: ${e.message}")
            }
        }
    }
}

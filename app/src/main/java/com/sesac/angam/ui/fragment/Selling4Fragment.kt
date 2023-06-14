package com.sesac.angam.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sesac.angam.GlobalApplication
import com.sesac.angam.R
import com.sesac.angam.base.BaseFragment
import com.sesac.angam.data.model.request.PostPickup
import com.sesac.angam.databinding.FragmentSelling3Binding
import com.sesac.angam.databinding.FragmentSelling4Binding
import com.sesac.angam.network.APIs
import com.sesac.angam.retrofit.RetrofitClient
import com.sesac.angam.ui.adapter.Selling3RecyclerViewAdapter
import com.sesac.angam.ui.viewmodel.Selling3ViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

class Selling4Fragment : BaseFragment<FragmentSelling4Binding>()  {

    private lateinit var retService: APIs
    private var accessToken = GlobalApplication.prefs.getString("userAccessToken", "")


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSelling4Binding {
        return FragmentSelling4Binding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}
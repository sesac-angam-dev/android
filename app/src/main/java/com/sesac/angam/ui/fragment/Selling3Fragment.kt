package com.sesac.angam.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sesac.angam.GlobalApplication
import com.sesac.angam.R
import com.sesac.angam.base.BaseFragment
import com.sesac.angam.data.model.request.PostPickup
import com.sesac.angam.databinding.FragmentSelling3Binding
import com.sesac.angam.network.APIs
import com.sesac.angam.retrofit.RetrofitClient
import com.sesac.angam.ui.adapter.RatingRecyclerViewAdapter
import com.sesac.angam.ui.adapter.Selling3RecyclerViewAdapter
import com.sesac.angam.ui.viewmodel.RatingViewModel
import com.sesac.angam.ui.viewmodel.Selling3ViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

class Selling3Fragment : BaseFragment<FragmentSelling3Binding>()  {

    private lateinit var retService: APIs
    private var accessToken = GlobalApplication.prefs.getString("userAccessToken", "")
    private lateinit var viewModel: Selling3ViewModel
    private lateinit var selling3RecyclerViewAdapter: Selling3RecyclerViewAdapter
    var postId = 0
    private val postIdList: MutableList<Int> = mutableListOf()


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSelling3Binding {
        return FragmentSelling3Binding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //getEstimate

        // ViewModel 초기화
        viewModel = ViewModelProvider(this).get(Selling3ViewModel::class.java)

        selling3RecyclerViewAdapter = Selling3RecyclerViewAdapter { task ->
            //click event 처리
            val id = task.postId
            if (postIdList.contains(id)) {
                postIdList.remove(id)
            } else {
                postIdList.add(id)
            }

        }

        binding.btnGoSelling3.setOnClickListener {
            // postIdList를 서버에 전송합니다.
            sendPostIdListToServer()
            Toast.makeText(requireContext(), "수거 요청이 신청되었습니다.", Toast.LENGTH_SHORT).show()
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, Selling4Fragment())
                .addToBackStack(null)
                .commit()
        }

        // recyclerview 구성
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = selling3RecyclerViewAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        // ViewModel과 RecyclerView 어댑터 연결
        viewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            selling3RecyclerViewAdapter.updateTasks(tasks)
        })

    }

    private fun sendPostIdListToServer() {
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIs::class.java)

        val request = PostPickup(idList = postIdList)

        lifecycleScope.launch {
            try {
                val response = retService.postPickup("Bearer $accessToken", request)
                // 성공적으로 서버에 전송되었을 때의 처리
                Log.d("Selling3Fragment post 통신 성공", "PostIdList sent successfully: ${request}")
            } catch (e: HttpException) {
                // We had a non-2XX HTTP error
                Toast.makeText(requireContext(), "Post failed", Toast.LENGTH_SHORT).show()
                Log.d("Selling3Fragment post 통신 실패", "요청 실패: " + e.message)

            } catch (e: Exception) {
                // A network error happened
                Toast.makeText(requireContext(), "Post failed", Toast.LENGTH_SHORT).show()
                Log.d("Selling3Fragment post 통신 실패", "전송 실패" + e.message)
            }
        }
    }

}
package com.sesac.angam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sesac.angam.GlobalApplication
import com.sesac.angam.R
import com.sesac.angam.base.BaseFragment
import com.sesac.angam.databinding.FragmentHomeBinding
import com.sesac.angam.ui.adapter.HotRecyclerViewAdapter
import com.sesac.angam.ui.adapter.RecommendRecyclerViewAdapter
import com.sesac.angam.ui.viewmodel.HotViewModel
import com.sesac.angam.ui.viewmodel.RecommendViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private lateinit var hotViewModel: HotViewModel
    private lateinit var hotRecyclerViewAdapter: HotRecyclerViewAdapter
    private lateinit var recommendViewModel: RecommendViewModel
    private lateinit var recommendRecyclerViewAdapter: RecommendRecyclerViewAdapter
    private var id = 0

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvPoint.text = GlobalApplication.prefs.getString("userPoint", "") + "원"
        // Hot 게시물
        // ViewModel 초기화
        hotViewModel = ViewModelProvider(this).get(HotViewModel::class.java)

        hotRecyclerViewAdapter = HotRecyclerViewAdapter { task ->
            //click event 처리
            val taskId = task.id.toInt()
            if (taskId != null) {
                id = taskId
                GlobalApplication.prefs.setString("postId", id.toString())
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, BuyerDetailFragment())
                    .addToBackStack(null)
                    .commit()
            } else {
                // Handle the case where the id is not a valid integer
                // For example, show an error message or perform a different action
            }
        }

        // recyclerview 구성
        val hotRecyclerView: RecyclerView = binding.hotRecyclerView
        hotRecyclerView.adapter = hotRecyclerViewAdapter
        val hotLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        hotRecyclerView.layoutManager = hotLayoutManager

        // ViewModel과 RecyclerView 어댑터 연결
        hotViewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            hotRecyclerViewAdapter.updateTasks(tasks)
        })

        // Recommend 게시물
        // ViewModel 초기화
        recommendViewModel = ViewModelProvider(this).get(RecommendViewModel::class.java)

        recommendRecyclerViewAdapter = RecommendRecyclerViewAdapter { task ->
            //click event 처리
            id = task.id
            GlobalApplication.prefs.setString("postId", id.toString())
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, BuyerDetailFragment())
                .addToBackStack(null)
                .commit()

        }

        // recyclerview 구성
        val recommendRecyclerView: RecyclerView = binding.recommendRecyclerView
        recommendRecyclerView.adapter = recommendRecyclerViewAdapter
        val recommendLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        recommendRecyclerView.layoutManager = recommendLayoutManager

        // ViewModel과 RecyclerView 어댑터 연결
        recommendViewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            recommendRecyclerViewAdapter.updateTasks(tasks)
        })

    }
}
package com.sesac.angam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sesac.angam.GlobalApplication
import com.sesac.angam.R
import com.sesac.angam.base.BaseFragment
import com.sesac.angam.databinding.FragmentMyPageBinding
import com.sesac.angam.ui.adapter.HotRecyclerViewAdapter
import com.sesac.angam.ui.adapter.MypageRecyclerViewAdapter
import com.sesac.angam.ui.viewmodel.HotViewModel
import com.sesac.angam.ui.viewmodel.MypageViewModel

class MyPageFragment : BaseFragment<FragmentMyPageBinding>()  {

    private lateinit var mypageViewModel: MypageViewModel
    private lateinit var mypageViewAdapter: MypageRecyclerViewAdapter
    private lateinit var hotViewModel: HotViewModel
    private lateinit var hotRecyclerViewAdapter: HotRecyclerViewAdapter

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMyPageBinding {
        return FragmentMyPageBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Hot 게시물
        // ViewModel 초기화
        mypageViewModel = ViewModelProvider(this).get(MypageViewModel::class.java)

        mypageViewAdapter = MypageRecyclerViewAdapter { task ->
            //click event 처리
            val taskId = task.postId
            if (taskId != null) {
                var id = taskId
                GlobalApplication.prefs.setString("postId", id.toString())
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, SellerDetailFragment())
                    .addToBackStack(null)
                    .commit()
            } else {
                // Handle the case where the id is not a valid integer
                // For example, show an error message or perform a different action
            }
        }

        // recyclerview 구성
        val mypageRecyclerView: RecyclerView = binding.RecyclerView
        mypageRecyclerView.adapter = mypageViewAdapter
        val mypageLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mypageRecyclerView.layoutManager = mypageLayoutManager

        // ViewModel과 RecyclerView 어댑터 연결
        mypageViewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            mypageViewAdapter.updateTasks(tasks)
        })



        // Hot 게시물
        // ViewModel 초기화
        hotViewModel = ViewModelProvider(this).get(HotViewModel::class.java)

        hotRecyclerViewAdapter = HotRecyclerViewAdapter { task ->
            //click event 처리
            val taskId = task.id.toInt()
            if (taskId != null) {
                var id = taskId
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








    }

}
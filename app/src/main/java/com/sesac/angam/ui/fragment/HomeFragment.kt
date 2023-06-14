package com.sesac.angam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sesac.angam.base.BaseFragment
import com.sesac.angam.databinding.FragmentHomeBinding
import com.sesac.angam.ui.adapter.HotRecyclerViewAdapter
import com.sesac.angam.ui.viewmodel.HotViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>()  {

    private lateinit var hotViewModel: HotViewModel
    private lateinit var hotRecyclerViewAdapter: HotRecyclerViewAdapter
    private var id = 0

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel 초기화
        hotViewModel = ViewModelProvider(this).get(HotViewModel::class.java)

        hotRecyclerViewAdapter = HotRecyclerViewAdapter { task ->
            //click event 처리
            id = task.id
        }

        // recyclerview 구성
        val recyclerView: RecyclerView = binding.hotRecyclerView
        recyclerView.adapter = hotRecyclerViewAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        // ViewModel과 RecyclerView 어댑터 연결
        hotViewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            hotRecyclerViewAdapter.updateTasks(tasks)
        })




    }
}
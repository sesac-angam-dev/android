package com.sesac.angam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.adapters.RatingBarBindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sesac.angam.GlobalApplication
import com.sesac.angam.R
import com.sesac.angam.databinding.FragmentRatingBinding
import com.sesac.angam.ui.adapter.RatingRecyclerViewAdapter
import com.sesac.angam.ui.viewmodel.RatingViewModel

class RatingFragment : Fragment()  {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var binding: com.sesac.angam.databinding.FragmentRatingBinding
    private var accessToken = GlobalApplication.prefs.getString("userAccessToken", "")
    private lateinit var viewModel: RatingViewModel
    private lateinit var ratingRecyclerViewAdapter: RatingRecyclerViewAdapter
    var postId = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        binding = FragmentRatingBinding.inflate(inflater)
        val view = binding.root

        // ViewModel 초기화
        viewModel = ViewModelProvider(this).get(RatingViewModel::class.java)

        ratingRecyclerViewAdapter = RatingRecyclerViewAdapter { task ->
            //click event 처리
            postId = task.postId
            var name = task.title
            var brand = task.brand
            binding.bottomTvName.text = name
            binding.bottomTvBrand.text = "["+brand+"]"

        }

        // recyclerview 구성
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = ratingRecyclerViewAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        // SnapHelper 설정
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        // ViewModel과 RecyclerView 어댑터 연결
        viewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            ratingRecyclerViewAdapter.updateTasks(tasks)
        })

        binding.btnRating.setOnClickListener {
            Toast.makeText(context, "평가 완료!", Toast.LENGTH_SHORT).show()
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, RatingFragment())
                .addToBackStack(null)
                .commit()
        }


        //bottom sheet
        val bottomSheet = binding.ratingBottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                bottomSheetBehavior.saveFlags = BottomSheetBehavior.SAVE_FIT_TO_CONTENTS
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        // 접혀있는 상태
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        // 드래깅 되고 있는 상태
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        // 완전히 펼쳐진 상태
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        // 절반으로 펼쳐진 상태
                    }
//                    BottomSheetBehavior.STATE_HIDDEN -> {
//                        // 아래로 숨겨진 상태
//                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                        // 드래그/스와이프 직후 고정된 상태
                    }
                }
            }


            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // This method is called when the Bottom Sheet is being dragged or settled (when the user's interaction with it is over).
            }
        })

        return view
    }

}
package com.sesac.angam.ui.fragment

import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sesac.angam.GlobalApplication
import com.sesac.angam.R
import com.sesac.angam.base.BaseFragment
import com.sesac.angam.databinding.FragmentBuyerDetailBinding
import com.sesac.angam.network.APIs
import com.sesac.angam.retrofit.RetrofitClient
import com.sesac.angam.ui.adapter.BiddingRecyclerViewAdapter
import com.sesac.angam.ui.adapter.HotRecyclerViewAdapter
import com.sesac.angam.ui.adapter.RatingRecyclerViewAdapter
import com.sesac.angam.ui.viewmodel.BiddingViewModel
import com.sesac.angam.ui.viewmodel.HotViewModel
import com.sesac.angam.ui.viewmodel.RatingViewModel
import kotlinx.coroutines.launch

class BuyerDetailFragment : Fragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var binding: FragmentBuyerDetailBinding
    private lateinit var retService: APIs
    private var accessToken = GlobalApplication.prefs.getString("userAccessToken", "")
    private var postId = GlobalApplication.prefs.getString("postId", "").toLong()
    private lateinit var biddingViewModel: BiddingViewModel
    private lateinit var biddingRecyclerViewAdapter: BiddingRecyclerViewAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_buyer_detail, container, false)
        binding = FragmentBuyerDetailBinding.inflate(inflater, container, false)
        val userPoint = GlobalApplication.prefs.getString("userPoint", "150000")

        binding.bottomTvName.text = userPoint + "원"

        binding.btnRealBidding.setOnClickListener {
            Toast.makeText(context, "입찰이 완료되었습니다!", Toast.LENGTH_SHORT).show()
            var point = userPoint.toInt() - 1000
            GlobalApplication.prefs.setString("userPoint", point.toString())
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, BuyerDetailFragment())
                .addToBackStack(null)
                .commit()
        }

        //heart
        var heart = false

        binding.ivHeart.setOnClickListener {
            if (heart) {
                binding.ivHeart.setImageResource(R.drawable.ic_heart_off)
                heart = false
                Toast.makeText(context, "찜 목록에서 삭제되었습니다!", Toast.LENGTH_SHORT).show()
            } else {
                binding.ivHeart.setImageResource(R.drawable.ic_heart_on)
                heart = true
                Toast.makeText(context, "찜 목록에 추가되었습니다!", Toast.LENGTH_SHORT).show()
            }
        }

        //서버 data
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIs::class.java)

        lifecycleScope.launch {
            try {
                val response = retService.getDetail("Bearer $accessToken", postId)
                if (response.isSuccessful) {
                    Log.d("BuyerDetailFragment API Success", "fetchTasks: ${response.body()}")
                    binding.tvBrand.text = "[" + response.body()?.brand + "]"
                    binding.tvName.text = response.body()?.title
                    binding.tvPrice.text = response.body()?.purchasePrice.toString() + "원"
                    binding.tvSize.text = response.body()?.size.toString()
                    binding.tvHistory.text = response.body()?.history
                    binding.tvWearNum.text = response.body()?.wearNum.toString()
                    binding.tvSellerName.text = response.body()?.userName

                    if(response.body()?.isSeller == true) {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, SellerDetailFragment())
                                .addToBackStack(null)
                                .commit()
                    }

                    if(response.body()?.liked == true) {
                        binding.ivHeart.setImageResource(R.drawable.ic_heart_on)
                    } else {
                        binding.ivHeart.setImageResource(R.drawable.ic_heart_off)
                    }

                    if(!response.body()?.postImage.isNullOrEmpty()) {
                        Glide.with(requireContext())
                            .load(response.body()?.postImage)
                            .into(binding.ivClothesImage)
                    }

                    if(!response.body()?.userImage.isNullOrEmpty()) {
                        Glide.with(requireContext())
                            .load(response.body()?.userImage)
                            .into(binding.ivSellerImage)
                    }

                    //keyword
                    binding.keyword1.visibility = View.INVISIBLE
                    binding.keyword2.visibility = View.INVISIBLE
                    binding.keyword3.visibility = View.INVISIBLE

                    val keywordList = response.body()?.keywordList
                    if (keywordList != null && keywordList.size > 0) {
                        if (keywordList[0].isNotEmpty()) {
                            binding.keyword1.visibility = View.VISIBLE
                            binding.keyword1.text = keywordList[0]
                        }
                        if (keywordList.size > 1 && keywordList[1].isNotEmpty()) {
                            binding.keyword2.visibility = View.VISIBLE
                            binding.keyword2.text = keywordList[1]
                        }
                        if (keywordList.size > 2 && keywordList[2].isNotEmpty()) {
                            binding.keyword3.visibility = View.VISIBLE
                            binding.keyword3.text = keywordList[2]
                        }
                    }


                } else {
                    Log.d("BuyerDetailFragment API Fail1", "fetchTasks: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.d("BuyerDetailFragment API Fail2", "fetchTasks: ${e.message}")
            }
        }

        //bidding
        // ViewModel 초기화
        biddingViewModel = ViewModelProvider(this).get(BiddingViewModel::class.java)
        biddingRecyclerViewAdapter = BiddingRecyclerViewAdapter { task ->
            //click event 처리
        }

        // recyclerview 구성
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = biddingRecyclerViewAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        // ViewModel과 RecyclerView 어댑터 연결
        biddingViewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            biddingRecyclerViewAdapter.updateTasks(tasks)
        })




        binding.bottomSheet.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        //bottom sheet 초기화
        val bottomSheet: ConstraintLayout = view.findViewById(R.id.bottomSheet)
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

        return binding.root
    }
}
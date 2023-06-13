package com.sesac.angam.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sesac.angam.R
import com.sesac.angam.base.BaseFragment

class BuyerDetailFragment : Fragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_buyer_detail, container, false)
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

        return view
    }
}
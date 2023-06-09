package com.sesac.angam.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sesac.angam.R
import com.sesac.angam.base.BaseFragment
import com.sesac.angam.databinding.FragmentSelling1Binding
import com.sesac.angam.databinding.FragmentSellingBinding

class Selling1Fragment : BaseFragment<FragmentSelling1Binding>()  {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSelling1Binding {
        return FragmentSelling1Binding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGoSelling2.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, Selling2Fragment())
                .addToBackStack(null)
                .commit()
        }

    }

}
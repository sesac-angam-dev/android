package com.sesac.angam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sesac.angam.base.BaseFragment
import com.sesac.angam.databinding.FragmentSellingBinding

class SellingFragment : BaseFragment<FragmentSellingBinding>()  {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSellingBinding {
        return FragmentSellingBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}
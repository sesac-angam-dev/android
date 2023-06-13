package com.sesac.angam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sesac.angam.base.BaseFragment
import com.sesac.angam.databinding.FragmentBuyerDetailBinding
import com.sesac.angam.databinding.FragmentSellerDetailBinding

class SellerDetailFragment : BaseFragment<FragmentSellerDetailBinding>()  {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSellerDetailBinding {
        return FragmentSellerDetailBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}
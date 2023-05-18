package com.chaehyun.project.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chaehyun.project.base.BaseFragment
import com.chaehyun.project.databinding.FragmentQrBinding


class QrFragment : BaseFragment<FragmentQrBinding>()  {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentQrBinding {
        return FragmentQrBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}
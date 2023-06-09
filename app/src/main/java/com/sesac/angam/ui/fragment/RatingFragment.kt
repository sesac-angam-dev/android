package com.sesac.angam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sesac.angam.base.BaseFragment
import com.sesac.angam.databinding.FragmentRatingBinding

class RatingFragment : BaseFragment<FragmentRatingBinding>()  {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRatingBinding {
        return FragmentRatingBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}
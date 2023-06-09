package com.sesac.angam.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.sesac.angam.R
import com.sesac.angam.base.BaseFragment
import com.sesac.angam.databinding.FragmentSelling1Binding
import com.sesac.angam.databinding.FragmentSelling2Binding

class Selling2Fragment : BaseFragment<FragmentSelling2Binding>()  {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSelling2Binding {
        return FragmentSelling2Binding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val sizeOptions = arrayOf("FREE", "XS", "S", "M", "L", "XL", "XXL")
        val adapter = ArrayAdapter(requireContext(), R.layout.rounded_spinner_dropdown_item, sizeOptions.toMutableList())
        adapter.setDropDownViewResource(R.layout.rounded_spinner_dropdown_item)
        binding.sizeSpinner.adapter = adapter

        binding.sizeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = sizeOptions[position]
                // 선택된 항목 처리

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무 항목도 선택되지 않았을 때 처리
            }
        }


    }

}
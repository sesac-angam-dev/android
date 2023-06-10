package com.sesac.angam.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import com.sesac.angam.GlobalApplication
import com.sesac.angam.R
import com.sesac.angam.base.BaseFragment
import com.sesac.angam.databinding.FragmentSelling1Binding
import com.sesac.angam.databinding.FragmentSelling2Binding

class Selling2Fragment : BaseFragment<FragmentSelling2Binding>()  {

    private var accessToken = GlobalApplication.prefs.getString("userAccessToken", "")

    private val info2LiveData = MutableLiveData<Boolean>()
    private val info3LiveData = MutableLiveData<Boolean>()
    private val info4LiveData = MutableLiveData<Boolean>()
    private val info5LiveData = MutableLiveData<Boolean>()

    private var info2 = "false"
    private var info3 = "false"
    private var info4 = "false"
    private var info5 = "false"
    private var goSelling = "false"

    private val prefsListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        when (key) {
            "info2" -> info2LiveData.value = GlobalApplication.prefs.getString("info2", "false") == "true"
            "info3" -> info3LiveData.value = GlobalApplication.prefs.getString("info3", "false") == "true"
            "info4" -> info4LiveData.value = GlobalApplication.prefs.getString("info4", "false") == "true"
            "info5" -> info5LiveData.value = GlobalApplication.prefs.getString("info5", "false") == "true"
        }
    }


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSelling2Binding {
        return FragmentSelling2Binding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        info2 = GlobalApplication.prefs.getString("info2", "false")
        info3 = GlobalApplication.prefs.getString("info3", "false")
        info4 = GlobalApplication.prefs.getString("info4", "false")
        info5 = GlobalApplication.prefs.getString("info5", "false")

        info2LiveData.value = info2 == "true"
        info3LiveData.value = info3 == "true"
        info4LiveData.value = info4 == "true"
        info5LiveData.value = info5 == "true"

        GlobalApplication.prefs.prefs.registerOnSharedPreferenceChangeListener(prefsListener)


        //info1 fragment
        parentFragmentManager.beginTransaction()
            .replace(R.id.info_fragment, Info1Fragment())
            .addToBackStack(null)
            .commit()

        binding.info1.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.info_fragment, Info1Fragment())
                .addToBackStack(null)
                .commit()
        }

        // info2 fragment
        info2LiveData.observe(viewLifecycleOwner) { info2Value ->
            if (info2Value) {
                goSelling = "true"
                binding.btnGoSelling3.setImageResource(R.drawable.btn_go_selling)
                binding.info2.setOnClickListener {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.info_fragment, Info2Fragment())
                        .addToBackStack(null)
                        .commit()
                    binding.info2.setImageResource(R.drawable.ic_info2_on)
                }
            } else {
                // info2가 false일 때의 처리
                // 버튼 클릭 이벤트 해제 또는 다른 처리 수행
            }
        }

        // info3 fragment
        info3LiveData.observe(viewLifecycleOwner) { info3Value ->
            if (info3Value) {
                binding.info3.setOnClickListener {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.info_fragment, Info3Fragment())
                        .addToBackStack(null)
                        .commit()
                    binding.info3.setImageResource(R.drawable.ic_info3_on)
                }
            } else {
                // info3가 false일 때의 처리
                // 버튼 클릭 이벤트 해제 또는 다른 처리 수행
            }
        }

        // info4 fragment
        info4LiveData.observe(viewLifecycleOwner) { info4Value ->
            if (info4Value) {
                binding.info4.setOnClickListener {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.info_fragment, Info4Fragment())
                        .addToBackStack(null)
                        .commit()
                    binding.info4.setImageResource(R.drawable.ic_info4_on)
                }
            } else {
                // info4가 false일 때의 처리
                // 버튼 클릭 이벤트 해제 또는 다른 처리 수행
            }
        }

        // info5 fragment
        info5LiveData.observe(viewLifecycleOwner) { info5Value ->
            if (info5Value) {
                binding.info5.setOnClickListener {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.info_fragment, Info5Fragment())
                        .addToBackStack(null)
                        .commit()
                    binding.info5.setImageResource(R.drawable.ic_info5_on)
                }
            } else {
                // info5가 false일 때의 처리
                // 버튼 클릭 이벤트 해제 또는 다른 처리 수행
            }
        }

        if(goSelling == "true") {
            binding.btnGoSelling3.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, Selling3Fragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}

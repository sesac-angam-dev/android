package com.sesac.angam.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sesac.angam.GlobalApplication
import com.sesac.angam.R
import com.sesac.angam.base.BaseFragment
import com.sesac.angam.data.model.request.PostData
import com.sesac.angam.data.model.request.PostItem
import com.sesac.angam.databinding.FragmentSelling2Binding
import com.sesac.angam.network.APIs
import com.sesac.angam.retrofit.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import retrofit2.HttpException
import java.io.File

class Selling2Fragment : BaseFragment<FragmentSelling2Binding>()  {

    private lateinit var retService: APIs
    private var accessToken = GlobalApplication.prefs.getString("userAccessToken", "")

    private val info2LiveData = MutableLiveData<Boolean>()
    private val info3LiveData = MutableLiveData<Boolean>()
    private val info4LiveData = MutableLiveData<Boolean>()
    private val info5LiveData = MutableLiveData<Boolean>()

    private var info2 = GlobalApplication.prefs.getString("info2", "false")
    private var info3 = GlobalApplication.prefs.getString("info3", "false")
    private var info4 = GlobalApplication.prefs.getString("info4", "false")
    private var info5 = GlobalApplication.prefs.getString("info5", "false")

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
        //retrofit
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIs::class.java)

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
                binding.btnGoSelling3.setImageResource(R.drawable.btn_go_selling)
                binding.info2.setOnClickListener {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.info_fragment, Info2Fragment())
                        .addToBackStack(null)
                        .commit()
                    binding.info2.setImageResource(R.drawable.ic_info2_on)
                }
                binding.btnGoSelling3.setOnClickListener {
                    Log.d("goSelling", "goSelling")
                    CoroutineScope(Dispatchers.Main).launch {
                        postInfo()
                    }
                }
            } else {
                //
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
                binding.btnGoSelling3.setOnClickListener {
                    Log.d("goSelling", "goSelling")
                    CoroutineScope(Dispatchers.Main).launch {
                        postInfo()
                    }
                }
            } else {
                //
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
                binding.btnGoSelling3.setOnClickListener {
                    Log.d("goSelling", "goSelling")
                    CoroutineScope(Dispatchers.Main).launch {
                        postInfo()
                    }
                }
            } else {
                //
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
                binding.btnGoSelling3.setOnClickListener {
                    Log.d("goSelling", "goSelling")
                    CoroutineScope(Dispatchers.Main).launch {
                        postInfo()
                    }
                }
            } else {
                //
            }
        }

    }

    suspend fun postInfo() {
        try {
            var infoStatus = GlobalApplication.prefs.getString("infoStatus", "").toInt()

            // 필드를 저장할 맵 생성
            val fields = mutableMapOf<String, RequestBody>()
            var imageParts = mutableListOf<MultipartBody.Part>()

            // infoStatus값만큼 반복
            for (i in 1..infoStatus) {
                val index = (i - 1).toString() // 인덱스를 생성
                val name = GlobalApplication.prefs.getString("name$i", "")
                val brand = GlobalApplication.prefs.getString("brand$i", "")
                val size = GlobalApplication.prefs.getString("size$i", "")
                val price = GlobalApplication.prefs.getString("price$i", "").toInt()
                val count = GlobalApplication.prefs.getString("count$i", "")
                val history = GlobalApplication.prefs.getString("history$i", "")
                val keywords = mutableListOf<String>()
                val imageFilePath = GlobalApplication.prefs.getString("imageFile$i", "")

                // 필드 값들을 RequestBody로 변환하여 맵에 추가
                fields["postList[$index].brand"] = brand.toRequestBody("text/plain".toMediaTypeOrNull())
                fields["postList[$index].history"] = history.toRequestBody("text/plain".toMediaTypeOrNull())
                fields["postList[$index].purchasePrice"] = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                fields["postList[$index].size"] = size.toRequestBody("text/plain".toMediaTypeOrNull())
                fields["postList[$index].title"] = name.toRequestBody("text/plain".toMediaTypeOrNull())
                fields["postList[$index].wearNum"] = count.toRequestBody("text/plain".toMediaTypeOrNull())

                val keywordsArray = JSONArray()
                for (j in 1..3) {
                    val keyword = GlobalApplication.prefs.getString("keyword$i$j", "")
                    if (keyword.isNotEmpty()) {
                        keywordsArray.put(keyword)
                    }
                }
                fields["postList[$index].keywords"] = keywordsArray.toString().toRequestBody("application/json".toMediaTypeOrNull())

                // 이미지 파일이 존재할 경우에만 처리
                if (imageFilePath.isNotEmpty()) {
                    val imageFile = File(imageFilePath)
                    val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData(
                        "postList[$index].image",  // 키에 인덱스 추가
                        imageFile.name,
                        requestBody
                    )
                    imageParts.add(imagePart)
                }
            }

            // 각 post에 대한 모든 정보가 준비되면 요청을 보냅니다.
            retService.infoPost("Bearer $accessToken", fields, imageParts)

            // If we get here, then the post was a success
            Log.d("selling 통신 성공", "selling post 통신 성공, 요청 성공")
            Toast.makeText(requireContext(), "Post Success", Toast.LENGTH_SHORT).show()
            Log.d("selling post 통신 성공", "요청 성공: " + fields.toString())

            // Selling3으로 이동
            // parentFragmentManager.beginTransaction()
            //    .replace(R.id.main_frm, Selling3Fragment())
            //    .addToBackStack(null)
            //    .commit()

        } catch (e: HttpException) {
            // We had a non-2XX HTTP error
            Toast.makeText(requireContext(), "Post failed", Toast.LENGTH_SHORT).show()
            Log.d("selling post 통신 실패", "요청 실패: " + e.message)

        } catch (e: Exception) {
            // A network error happened
            Toast.makeText(requireContext(), "Post failed", Toast.LENGTH_SHORT).show()
            Log.d("selling post 통신 실패", "전송 실패" + e.message)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        GlobalApplication.prefs.prefs.unregisterOnSharedPreferenceChangeListener(prefsListener)
    }
}

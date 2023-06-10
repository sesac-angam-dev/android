package com.sesac.angam.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.sesac.angam.GlobalApplication
import com.sesac.angam.R
import com.sesac.angam.base.BaseFragment
import com.sesac.angam.databinding.FragmentInfo1Binding
import com.sesac.angam.databinding.FragmentRatingBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class Info1Fragment : BaseFragment<FragmentInfo1Binding>()  {

    private val REQUEST_IMAGE_PICK = 1
    private val REQUEST_PERMISSION = 2
    var true1 = false
    var true2 = false
    var true3 = false

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentInfo1Binding {
        return FragmentInfo1Binding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPhoto.setOnClickListener {
            // 갤러리에서 사진 선택을 요청
            checkPermissionAndPickImage()
        }

        binding.keyword1.visibility = View.GONE
        binding.keyword2.visibility = View.GONE
        binding.keyword3.visibility = View.GONE

        binding.tvName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                var name = binding.tvName.text.toString()
                true1 = name.isNotEmpty()
                checkAndSetPreferences()
            }
        })

        binding.tvBrand.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                var brand = binding.tvBrand.text.toString()
                true2 = brand.isNotEmpty()
                checkAndSetPreferences()
            }
        })

        binding.tvPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                var price = binding.tvPrice.text.toString()
                true3 = price.isNotEmpty()
                checkAndSetPreferences()
            }
        })

        //history 필수 x
        binding.tvHistory.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                var history = binding.tvHistory.text.toString()
            }
        })

        // size spinner
        val sizeOptions = arrayOf("FREE", "XS", "S", "M", "L", "XL", "XXL")
        val sizeAdapter = ArrayAdapter(requireContext(), R.layout.rounded_spinner_dropdown_item, sizeOptions.toMutableList())
        sizeAdapter.setDropDownViewResource(R.layout.rounded_spinner_dropdown_item)
        binding.sizeSpinner.adapter = sizeAdapter
        // click listener
        binding.sizeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = sizeOptions[position]
                // 선택된 항목 처리
                var size = selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무 항목도 선택되지 않았을 때 처리
            }
        }

        // count spinner
        val countOptions = arrayOf("0회", "5회 미만", "10회 이상", "30회 이상")
        val countAdapter = ArrayAdapter(requireContext(), R.layout.rounded_spinner_dropdown_item, countOptions.toMutableList())
        countAdapter.setDropDownViewResource(R.layout.rounded_spinner_dropdown_item)
        binding.countSpinner.adapter = countAdapter
        // click listener
        binding.countSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem1 = countOptions[position]
                // 선택된 항목 처리
                var count = selectedItem1
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무 항목도 선택되지 않았을 때 처리
            }
        }
    }

    private fun checkPermissionAndPickImage() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            pickImageFromGallery()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION)
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data
            if (selectedImageUri != null) {
                // 사진 업로드 요청
                uploadImage(selectedImageUri)
                // 선택된 이미지를 btnPhoto의 이미지로 설정
                binding.btnPhoto.setImageURI(selectedImageUri)
            }
        }
    }

    private fun uploadImage(uri: Uri) {
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val file = File(getRealPathFromUri(uri))

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("image", file.name, requestBody)

        Log.d("image", "uploadImage: $filePart")

        // val retrofitService = retrofit.create(RetrofitService::class.java)
        // val call = retrofitService.uploadImage(filePart)
        // call.enqueue(object : Callback<ApiResponse> {
        //     override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
        //         // 업로드 성공 처리
        //     }
        //
        //     override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
        //         // 업로드 실패 처리
        //     }
        // })
    }

    private fun getRealPathFromUri(uri: Uri): String? {
        var realPath: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = requireActivity().contentResolver.query(uri, proj, null, null, null)
        if (cursor?.moveToFirst() == true) {
            val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            realPath = cursor.getString(columnIndex)
        }
        cursor?.close()
        return realPath
    }

    private fun checkAndSetPreferences() {
        if (true1 && true2 && true3) {
            GlobalApplication.prefs.setString("info2", "true")
        } else {
            GlobalApplication.prefs.setString("info2", "false")
        }
    }

}
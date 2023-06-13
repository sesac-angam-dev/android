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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.android.material.internal.ViewUtils
import com.sesac.angam.GlobalApplication
import com.sesac.angam.R
import com.sesac.angam.base.BaseFragment
import com.sesac.angam.databinding.FragmentInfo4Binding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class Info4Fragment : BaseFragment<FragmentInfo4Binding>()  {

    private val REQUEST_IMAGE_PICK = 1
    private val REQUEST_PERMISSION = 2
    var true1 = false
    var true2 = false
    var true3 = false
    var keywordNum = 1

    //임시저장 data가져오기
    var name = GlobalApplication.prefs.getString("name4", "")
    var imageFile = GlobalApplication.prefs.getString("imageFile4", "")
    var imageUri = GlobalApplication.prefs.getString("imageUri4", "")
    var brand = GlobalApplication.prefs.getString("brand4", "")
    var size = GlobalApplication.prefs.getString("size4", "")
    var price = GlobalApplication.prefs.getString("price4", "")
    var countNum = GlobalApplication.prefs.getString("count4", "")
    var keyword1 = GlobalApplication.prefs.getString("keyword41", "")
    var keyword2 = GlobalApplication.prefs.getString("keyword42", "")
    var keyword3 = GlobalApplication.prefs.getString("keyword43", "")
    var history = GlobalApplication.prefs.getString("history4", "")


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentInfo4Binding {
        return FragmentInfo4Binding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnTouchListener { v, event ->
            ViewUtils.hideKeyboard(v)
            false
        }

        binding.keyword1.visibility = View.INVISIBLE
        binding.keyword2.visibility = View.INVISIBLE
        binding.keyword3.visibility = View.INVISIBLE

        binding.btnPhoto.setOnClickListener {
            // 갤러리에서 사진 선택을 요청
            checkPermissionAndPickImage()
        }

        //임시저장 data 불러오기
        binding.tvName.setText(name)
        binding.tvBrand.setText(brand)
        binding.tvPrice.setText(price)
//        binding.tvKeyword.setText(keyword1)
        binding.tvHistory.setText(history)
        if(keyword1.isNotEmpty()) {
            binding.keyword1.visibility = View.VISIBLE
            binding.keyword1.text = keyword1
            keywordNum = 2
        }
        if(keyword2.isNotEmpty()) {
            binding.keyword2.visibility = View.VISIBLE
            binding.keyword2.text = keyword2
            keywordNum = 3
        }
        if(keyword3.isNotEmpty()) {
            binding.keyword3.visibility = View.VISIBLE
            binding.keyword3.text = keyword3
            keywordNum = 4
        }

        if(imageUri.isNotEmpty()){
            binding.btnPhoto.setImageURI(imageUri.toUri())
        }

        binding.tvName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                var name = binding.tvName.text.toString()
                GlobalApplication.prefs.setString("name4", name)
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
                var tvbrand = binding.tvBrand.text.toString()
                GlobalApplication.prefs.setString("brand4", tvbrand)
                true2 = tvbrand.isNotEmpty()
                checkAndSetPreferences()
            }
        })

        binding.tvPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                var tvprice = binding.tvPrice.text.toString()
                GlobalApplication.prefs.setString("price4", tvprice)
                true3 = tvprice.isNotEmpty()
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
                var tvhistory = binding.tvHistory.text.toString()
                GlobalApplication.prefs.setString("history4", tvhistory)
            }
        })

        binding.addKeyword.setOnClickListener {
            if(keywordNum == 1) {
                binding.keyword1.visibility = View.VISIBLE
                var tvKeyword = binding.tvKeyword.text.toString()
                binding.keyword1.text = tvKeyword
                GlobalApplication.prefs.setString("keyword41", tvKeyword)
                keywordNum++
                binding.tvKeyword.text = null
            } else if (keywordNum == 2) {
                binding.keyword2.visibility = View.VISIBLE
                binding.keyword2.text = binding.tvKeyword.text.toString()
                GlobalApplication.prefs.setString("keyword42", binding.tvKeyword.text.toString())
                keywordNum++
                binding.tvKeyword.text = null
            } else if (keywordNum == 3) {
                binding.keyword3.visibility = View.VISIBLE
                binding.keyword3.text = binding.tvKeyword.text.toString()
                GlobalApplication.prefs.setString("keyword43", binding.tvKeyword.text.toString())
                keywordNum++
                binding.tvKeyword.text = null
            } else {
                Toast.makeText(requireContext(), "키워드는 3개까지만 추가할 수 있습니다.", Toast.LENGTH_SHORT).show()
            }
        }

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
                var tvsize = selectedItem
                GlobalApplication.prefs.setString("size4", tvsize)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무 항목도 선택되지 않았을 때 처리
            }
        }
        if(size != "") {
            val desiredPosition = sizeOptions.indexOf(size)
            binding.sizeSpinner.setSelection(desiredPosition)
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
                var tvcount = selectedItem1
                GlobalApplication.prefs.setString("count4", tvcount)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무 항목도 선택되지 않았을 때 처리
            }
        }
        if(countNum != "") {
            val desiredPosition1 = countOptions.indexOf(countNum)
            binding.countSpinner.setSelection(desiredPosition1)
        }
    }

    private fun checkPermissionAndPickImage() {
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
        }
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(requireActivity(), permissionsToRequest.toTypedArray(), REQUEST_PERMISSION)
        } else {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                pickImageFromGallery()
            } else {
                // 권한 거부 시 사용자에게 설명하거나 앱 동작을 처리합니다.
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
                // 이미지 파일 경로를 저장
                GlobalApplication.prefs.setString("imageUri4", selectedImageUri.toString())
            }
        }
    }

    private fun uploadImage(uri: Uri) {
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val file = File(getRealPathFromUri(uri))

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("image", file.name, requestBody)

        // 이미지 파일 sharedPreference에 저장
        GlobalApplication.prefs.setString("imageFile4", file.absolutePath)
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
            GlobalApplication.prefs.setString("info5", "true")
            GlobalApplication.prefs.setString("infoStatus", "4")
        } else {
            GlobalApplication.prefs.setString("info5", "false")
        }
    }

}
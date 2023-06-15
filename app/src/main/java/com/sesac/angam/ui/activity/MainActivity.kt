package com.sesac.angam.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sesac.angam.GlobalApplication
import com.sesac.angam.R
import com.sesac.angam.databinding.ActivityMainBinding
import com.sesac.angam.databinding.FragmentBuyerDetailBinding
import com.sesac.angam.ui.fragment.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalApplication.prefs.setString("info2", "false")
        GlobalApplication.prefs.setString("info3", "false")
        GlobalApplication.prefs.setString("info4", "false")
        GlobalApplication.prefs.setString("info5", "false")
        GlobalApplication.prefs.setString("userAccessToken", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqZW5ueTAxMDkyNUBuYXZlci5jb20iLCJhdXRoIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjg2MTQ1NDE4LCJleHAiOjE2ODk3NDU0MTh9.xLSxBb8B9e0AgjkkLD5ciE4wxnVIUgqRnKbBbg3Ey0SE9tap7LrGR_xbaFZW2xyUyMQ7JYtDrNjk0pQsIcdvwQ")

        // 임시 저장 값 초기화
//        for (i in 1..5) {
//            GlobalApplication.prefs.setString("name$i", "")
//            GlobalApplication.prefs.setString("imageFile$i", "")
//            GlobalApplication.prefs.setString("imageUri$i", "")
//            GlobalApplication.prefs.setString("brand$i", "")
//            GlobalApplication.prefs.setString("size$i", "")
//            GlobalApplication.prefs.setString("price$i", "")
//            GlobalApplication.prefs.setString("count$i", "")
//            GlobalApplication.prefs.setString("history$i", "")
//            for (j in 1..3) {
//                GlobalApplication.prefs.setString("keyword$i$j", "")
//            }
//        }


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainBnv.itemIconTintList = null
        initBottomNavigation()

    }

    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, RatingFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.sellingFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, Selling2Fragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.ratingFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, RatingFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.mypageFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, MyPageFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}
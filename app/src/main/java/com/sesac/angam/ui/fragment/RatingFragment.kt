package com.sesac.angam.ui.fragment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.adapters.RatingBarBindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sesac.angam.GlobalApplication
import com.sesac.angam.R
import com.sesac.angam.databinding.FragmentRatingBinding
import com.sesac.angam.ui.activity.MainActivity
import com.sesac.angam.ui.adapter.RatingRecyclerViewAdapter
import com.sesac.angam.ui.viewmodel.RatingViewModel

class RatingFragment : Fragment()  {

    private lateinit var notificationManager: NotificationManager

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var binding: com.sesac.angam.databinding.FragmentRatingBinding
    private var accessToken = GlobalApplication.prefs.getString("userAccessToken", "")
    private lateinit var viewModel: RatingViewModel
    private lateinit var ratingRecyclerViewAdapter: RatingRecyclerViewAdapter
    var postId = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        binding = FragmentRatingBinding.inflate(inflater)
        val view = binding.root

        // NotificationManager 인스턴스 생성
        notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        binding.blank.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, Selling3Fragment())
                .addToBackStack(null)
                .commit()
        }
        binding.textView.setOnClickListener {
            if (areNotificationsEnabled()) {
                // 알림 권한이 이미 허용된 경우
                showNotification()
            } else {
                // 알림 권한을 요청
                requestNotificationPermission()
            }
        }

        // ViewModel 초기화
        viewModel = ViewModelProvider(this).get(RatingViewModel::class.java)

        ratingRecyclerViewAdapter = RatingRecyclerViewAdapter { task ->
            //click event 처리
            postId = task.postId
            var name = task.title
            var brand = task.brand
            binding.bottomTvName.text = name
            binding.bottomTvBrand.text = "["+brand+"]"

        }

        // recyclerview 구성
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = ratingRecyclerViewAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        // SnapHelper 설정
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        // ViewModel과 RecyclerView 어댑터 연결
        viewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            ratingRecyclerViewAdapter.updateTasks(tasks)
        })

        binding.btnRating.setOnClickListener {
            Toast.makeText(context, "평가 완료!", Toast.LENGTH_SHORT).show()
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, RatingFragment())
                .addToBackStack(null)
                .commit()
        }


        //bottom sheet
        val bottomSheet = binding.ratingBottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                bottomSheetBehavior.saveFlags = BottomSheetBehavior.SAVE_FIT_TO_CONTENTS
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        // 접혀있는 상태
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        // 드래깅 되고 있는 상태
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        // 완전히 펼쳐진 상태
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        // 절반으로 펼쳐진 상태
                    }
//                    BottomSheetBehavior.STATE_HIDDEN -> {
//                        // 아래로 숨겨진 상태
//                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                        // 드래그/스와이프 직후 고정된 상태
                    }
                }
            }


            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // This method is called when the Bottom Sheet is being dragged or settled (when the user's interaction with it is over).
            }
        })

        return view
    }


    private fun showNotification() {
        // 알림을 클릭했을 때 실행될 액티비티로 이동하기 위한 PendingIntent 생성
        val intent = Intent(requireContext(), MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // 알림 채널 생성 (Android 8.0 이상에서 필요)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "channel_id11"
            val channelName = "Channel Name"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(0, 100, 200, 300) // 진동 패턴 설정
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 생성
        val contentView = RemoteViews(requireContext().packageName, R.layout.custom_notification_layout1)

        val builder = NotificationCompat.Builder(requireContext(), "channel_id11")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Notification Title")
            .setContentText("Notification Text")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setCustomContentView(contentView)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE) // 카테고리 설정
            .setFullScreenIntent(pendingIntent, true) // 헤드업 알림 설정

        // 알림 표시
        val notificationId = 1
        notificationManager.notify(notificationId, builder.build())
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8.0 이상에서는 알림 채널 설정 페이지로 이동
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
            startActivity(intent)
        } else {
            // Android 7.1 이하에서는 알림 설정 페이지로 이동
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", requireContext().packageName, null)
            intent.data = uri
            startActivity(intent)
        }
    }

    private fun areNotificationsEnabled(): Boolean {
        val notificationManager = NotificationManagerCompat.from(requireContext())
        return notificationManager.areNotificationsEnabled()
    }

}
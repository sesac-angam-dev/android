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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sesac.angam.GlobalApplication
import com.sesac.angam.R
import com.sesac.angam.base.BaseFragment
import com.sesac.angam.databinding.FragmentHomeBinding
import com.sesac.angam.ui.activity.MainActivity
import com.sesac.angam.ui.adapter.HotRecyclerViewAdapter
import com.sesac.angam.ui.adapter.RecommendRecyclerViewAdapter
import com.sesac.angam.ui.viewmodel.HotViewModel
import com.sesac.angam.ui.viewmodel.RecommendViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private lateinit var hotViewModel: HotViewModel
    private lateinit var hotRecyclerViewAdapter: HotRecyclerViewAdapter
    private lateinit var recommendViewModel: RecommendViewModel
    private lateinit var recommendRecyclerViewAdapter: RecommendRecyclerViewAdapter
    private var id = 0
    private lateinit var notificationManager: NotificationManager

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // NotificationManager 인스턴스 생성
        notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        binding.imageView10.setOnClickListener {
            if (areNotificationsEnabled()) {
                // 알림 권한이 이미 허용된 경우
                showNotification()
            } else {
                // 알림 권한을 요청
                requestNotificationPermission()
            }
        }

        binding.tvPoint.text = GlobalApplication.prefs.getString("userPoint", "") + "원"
        // Hot 게시물
        // ViewModel 초기화
        hotViewModel = ViewModelProvider(this).get(HotViewModel::class.java)

        hotRecyclerViewAdapter = HotRecyclerViewAdapter { task ->
            //click event 처리
            val taskId = task.id.toInt()
            if (taskId != null) {
                id = taskId
                GlobalApplication.prefs.setString("postId", id.toString())
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, BuyerDetailFragment())
                    .addToBackStack(null)
                    .commit()
            } else {
                // Handle the case where the id is not a valid integer
                // For example, show an error message or perform a different action
            }
        }

        // recyclerview 구성
        val hotRecyclerView: RecyclerView = binding.hotRecyclerView
        hotRecyclerView.adapter = hotRecyclerViewAdapter
        val hotLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        hotRecyclerView.layoutManager = hotLayoutManager

        // ViewModel과 RecyclerView 어댑터 연결
        hotViewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            hotRecyclerViewAdapter.updateTasks(tasks)
        })

        // Recommend 게시물
        // ViewModel 초기화
        recommendViewModel = ViewModelProvider(this).get(RecommendViewModel::class.java)

        recommendRecyclerViewAdapter = RecommendRecyclerViewAdapter { task ->
            //click event 처리
            id = task.id
            GlobalApplication.prefs.setString("postId", id.toString())
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, BuyerDetailFragment())
                .addToBackStack(null)
                .commit()

        }

        // recyclerview 구성
        val recommendRecyclerView: RecyclerView = binding.recommendRecyclerView
        recommendRecyclerView.adapter = recommendRecyclerViewAdapter
        val recommendLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        recommendRecyclerView.layoutManager = recommendLayoutManager

        // ViewModel과 RecyclerView 어댑터 연결
        recommendViewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            recommendRecyclerViewAdapter.updateTasks(tasks)
        })

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
        val contentView = RemoteViews(requireContext().packageName, R.layout.custom_notification_layout)

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
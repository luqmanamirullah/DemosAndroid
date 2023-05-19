package com.example.demos.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.demos.R
import com.example.demos.databinding.ActivitySettingBinding
import androidx.fragment.app.Fragment

class SettingActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingBinding
    private lateinit var notificationManager: NotificationManager
    private val channelId = "DemoChannelId"
    private val channelName = "DemoChannel"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.notif.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                showNotification(
                    "Kontol",
                    "Kami mendapatkan informasi terkait pengguna bahwa kamu adalah kontol"
                )
                showToast("Notification is on")
            } else {
                showToast("Notification is off")
            }
        }
        binding.viewProfile.setOnClickListener {
            val intent = Intent(this, DetailProfileActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val mainActivity = Intent(this, MainActivity::class.java)
        mainActivity.putExtra("openFragmentProfile", true)
        startActivity(mainActivity)
        finish()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(title: String, message: String) {
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notifications)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
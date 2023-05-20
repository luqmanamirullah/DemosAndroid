package com.example.demos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.demos.databinding.ActivityChangePasswordBinding
import com.example.demos.databinding.ActivityForgotPasswordBinding

class ForgotPassword : AppCompatActivity() {
    lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSend.setOnClickListener {
            Toast.makeText(this,"We're sending your verification code please wait",Toast.LENGTH_SHORT).show()
        }
    }
}
package com.example.demos.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.widget.TextView
import android.widget.Toast
import com.example.demos.R
import com.example.demos.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityChangePasswordBinding
    private lateinit var txtForgetPassword: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnSave.setOnClickListener {
            onBackPressed().also {
                Toast.makeText(this, "Password Changed", Toast.LENGTH_SHORT).show()
            }
        }
        binding.txtForgotPassword.setOnClickListener {
            intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }

        txtForgetPassword = findViewById(R.id.txtForgotPassword)
        setUnderlineText("Forgot password")

    }
    private fun setUnderlineText(text: String) {
        val spannableString = SpannableString(text)
        spannableString.setSpan(UnderlineSpan(), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        txtForgetPassword.text = spannableString
    }
}
package com.example.demos.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.widget.TextView
import com.example.demos.R
import com.example.demos.databinding.ActivityLoginBinding
import com.example.demos.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var txtForgetPassword: TextView
    private lateinit var txtSignUp: TextView

    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Initialize the TextView
        txtForgetPassword = findViewById(R.id.txtForgotPassword)
        txtSignUp = findViewById(R.id.txtSignUp)

        // Set underline text
        setUnderlineText1("Forgot Password")
        setUnderlineText2("Sign Up")

        return
    }

    private fun setUnderlineText1(text: String) {
        val spannableString = SpannableString(text)
        spannableString.setSpan(UnderlineSpan(), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        txtForgetPassword.text = spannableString
    }

    private fun setUnderlineText2(text: String) {
        val spannableString = SpannableString(text)
        spannableString.setSpan(UnderlineSpan(), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        txtSignUp.text = spannableString
    }
}
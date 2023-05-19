package com.example.demos.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.demos.R
import com.example.demos.databinding.ActivityInformationBinding
import com.example.demos.databinding.ActivityMainBinding

class InformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
        binding = ActivityInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnOK.setOnClickListener {
            intent = Intent (this, MainActivity::class.java).also {
                startActivity(it)
            }
        }
    }
    override fun onBackPressed() {
        val mainActivity = Intent(this, MainActivity::class.java)
        mainActivity.putExtra("openFragmentProfile", true)
        startActivity(mainActivity)
        finish()
    }
}
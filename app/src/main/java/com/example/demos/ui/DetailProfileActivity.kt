package com.example.demos.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.example.demos.R
import com.example.demos.databinding.ActivityDetailProfileBinding
import com.example.demos.ui.fragments.ProfileFragment

class DetailProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener {
            intent = Intent (this, MainActivity::class.java).also {
                startActivity(it)
            }
        }
        binding.btnSave.setOnClickListener {
            onBackPressed().also {
                Toast.makeText(this, "Profile Saved",Toast.LENGTH_SHORT).show()
            }
        }
    }

}
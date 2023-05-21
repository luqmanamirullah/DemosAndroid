package com.example.demos.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.demos.databinding.ActivityDetailProfileBinding
import com.example.demos.ui.fragments.ProfileFragment
import com.example.demos.utils.SessionManager

class DetailProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProfileBinding
    private val REQUEST_IMAGE_GALLERY = 1
    private var selectedImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SessionManager.getCurentUser(this).let {
            binding.apply {
                if (it.user_name !== null){
                    etUsername.hint = it.user_name
                }

                if (it.user_email !== null){
                    etEmail.hint = it.user_email
                }

                if (it.user_bio !== null){
                    etBio.hint = it.user_bio
                }

                if (it.user_phone !== null){
                    etPhone.hint = it.user_phone
                }

                if (it.user_photo !== null){
                    Glide.with(this@DetailProfileActivity).load(it.user_photo).into(ivProfile)
                }
            }
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnSave.setOnClickListener {
            onBackPressed().also {
                Toast.makeText(this, "Profile Saved",Toast.LENGTH_SHORT).show()
            }
        }
        binding.ivProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
        }
    }
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.putExtra("openFragmentProfile", true)
        intent.putExtra("profileImageUri", selectedImageUri)
        startActivity(intent)
        finish()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val profileFragment = supportFragmentManager.findFragmentByTag("ProfileFragment") as? ProfileFragment
        profileFragment?.updateProfilePhoto(selectedImageUri)
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            binding.ivProfile.setImageURI(selectedImageUri)
            profileFragment?.updateProfilePhoto(selectedImageUri)
        }
    }
}
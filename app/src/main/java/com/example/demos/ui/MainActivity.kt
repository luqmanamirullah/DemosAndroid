package com.example.demos.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.demos.R
import com.example.demos.database.NewsDatabase
import com.example.demos.databinding.ActivityMainBinding
import com.example.demos.repository.HomeRepository
import com.example.demos.ui.fragments.ProfileFragment
import com.example.demos.ui.viewmodels.HomeViewModel
import com.example.demos.ui.viewmodels.HomeViewModelProviderFactory
import com.example.demos.ui.viewmodels.LoginViewModel

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var homeViewModel: HomeViewModel
    private val REQUEST_IMAGE_GALLERY = 1
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val homeRepository = HomeRepository(NewsDatabase(this))
        homeViewModel = ViewModelProvider(
            this,
            HomeViewModelProviderFactory(homeRepository)
        )[HomeViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.frActivity)
        binding.bottomNavigationView.setupWithNavController(navController)

        val openFragmentProfile = intent.getBooleanExtra("openFragmentProfile", false)
        if (openFragmentProfile) {
            navController.navigate(R.id.profileFragment)
        }
    }
    private fun openProfileFragment() {
        val fragmentProfile = ProfileFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.flFragment, fragmentProfile)
            .commit()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            val profileFragment = supportFragmentManager.findFragmentByTag("ProfileFragment") as? ProfileFragment
            profileFragment?.updateProfilePhoto(selectedImageUri)
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
}
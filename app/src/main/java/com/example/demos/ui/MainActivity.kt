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
import com.example.demos.repository.NewsRepository
import com.example.demos.repository.PolicyRepository
import com.example.demos.ui.fragments.ProfileFragment
import com.example.demos.ui.viewmodels.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var homeViewModel: HomeViewModel
    lateinit var policyViewModel: PolicyViewModel
    lateinit var newsViewModel: NewsViewModel
    private val REQUEST_IMAGE_GALLERY = 1
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val homeRepository = HomeRepository()
        homeViewModel = ViewModelProvider(
            this,
            HomeViewModelProviderFactory(homeRepository, application)
        )[HomeViewModel::class.java]

        val policyRepository  = PolicyRepository()
        policyViewModel = ViewModelProvider(
            this,
            PolicyViewModelProviderFactory(policyRepository, application)
        )[PolicyViewModel::class.java]

        val newsRepository = NewsRepository(NewsDatabase(this))
        newsViewModel = ViewModelProvider(
            this,
            NewsViewModelProviderFactory(newsRepository)
        )[NewsViewModel::class.java]

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.frActivity)
        binding.bottomNavigationView.setupWithNavController(navController)

        val openFragementPolicy = intent.getBooleanExtra("openFragmentPolicy", false)
        val openFragmentProfile = intent.getBooleanExtra("openFragmentProfile", false)
        val openFragmentHome = intent.getBooleanExtra("openFragmentHome", false)

        if (openFragmentProfile) {
            navController.navigate(R.id.profileFragment)
        }
        if (openFragementPolicy){
            navController.navigate(R.id.govermentPolicyFragment)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            val profileFragment = supportFragmentManager.findFragmentByTag("ProfileFragment") as? ProfileFragment
            profileFragment?.updateProfilePhoto(selectedImageUri)
        }
    }
//    override fun onBackPressed() {
//        val intent = Intent(this, MainActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//        intent.putExtra("openFragmentProfile", true)
//        intent.putExtra("profileImageUri", selectedImageUri)
//        startActivity(intent)
//        finish()
//    }
}
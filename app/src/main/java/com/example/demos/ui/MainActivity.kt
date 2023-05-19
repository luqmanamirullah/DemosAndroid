package com.example.demos.ui

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            openProfileFragment()
        }
    }
    private fun openProfileFragment() {
        val fragmentProfile = ProfileFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.flFragment, fragmentProfile)
            .commit()
    }
}
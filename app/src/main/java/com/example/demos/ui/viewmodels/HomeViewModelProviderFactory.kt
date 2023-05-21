package com.example.demos.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.demos.repository.HomeRepository

class HomeViewModelProviderFactory(val homeRepository: HomeRepository, val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
           return HomeViewModel(homeRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
package com.example.demos.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.demos.repository.HomeRepository

class HomeViewModelProviderFactory(private val homeRepository: HomeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
           return HomeViewModel(homeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
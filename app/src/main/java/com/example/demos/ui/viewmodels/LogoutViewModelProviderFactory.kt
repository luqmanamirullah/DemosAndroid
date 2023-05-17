package com.example.demos.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.demos.repository.LogoutRepository


class LogoutViewModelProviderFactory(
    private val logoutRepository: LogoutRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LogoutViewModel::class.java)){
            return LogoutViewModel(logoutRepository) as T
        }
        throw IllegalArgumentException("Unknown type view model")
    }
}
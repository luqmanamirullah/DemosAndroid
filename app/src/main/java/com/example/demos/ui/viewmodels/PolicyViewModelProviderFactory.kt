package com.example.demos.ui.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.demos.repository.PolicyRepository

class PolicyViewModelProviderFactory(
    private val policyRepository: PolicyRepository,
    val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PolicyViewModel::class.java)){
            return PolicyViewModel(policyRepository, application) as T
        }
        throw IllegalArgumentException("Unknown view model type")
    }

}
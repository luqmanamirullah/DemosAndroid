package com.example.demos.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.demos.repository.NewsRepository
import com.example.demos.repository.SearchRepository

class SearchViewModelProviderFactory(val searchRepository: SearchRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(searchRepository) as T
        }
        throw IllegalArgumentException("Unknown View Model Type")
    }
}
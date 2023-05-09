package com.example.demos.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.demos.repository.ArticleRepository
import com.example.demos.repository.HomeRepository

class ArticleViewModelProviderFactory(private val articleRepository: ArticleRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleViewModel::class.java)){
           return ArticleViewModel(articleRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
package com.example.demos.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demos.models.newsFromInternet.NewsInternet
import com.example.demos.repository.NewsRepository
import com.example.demos.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response


class NewsViewModel(val newsRepository: NewsRepository): ViewModel() {
    val news: MutableLiveData<Resource<NewsInternet>> = MutableLiveData()


    fun getNews(category: String,q: String = "") = viewModelScope.launch{
        news.postValue(Resource.Loading())
        val response = newsRepository.getNews(category,q)
        news.postValue(handleGetNews(response))
    }

    private fun handleGetNews(response: Response<NewsInternet>): Resource<NewsInternet>{
        if (response.isSuccessful){
            response.body()?.let { res ->
                return Resource.Success(res)
            }
        }
        return Resource.Error(response.message())
    }
}
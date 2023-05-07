package com.example.demos.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demos.models.news.NewsLists
import com.example.demos.repository.HomeRepository
import com.example.demos.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response


class HomeViewModel(
    val homeRepository: HomeRepository
): ViewModel() {

    val news: MutableLiveData<Resource<NewsLists>> = MutableLiveData()

    init {
        getNews()
    }

    fun getNews() = viewModelScope.launch {
        news.postValue(Resource.Loading())
        val response = homeRepository.getNews()
        news.postValue(handelNews(response))
    }

    private fun handelNews(response: Response<NewsLists>): Resource<NewsLists>{
        if (response.isSuccessful){
            response.body()?.let { res ->
                return Resource.Success(res)
            }
        }
        return Resource.Error(response.message())
    }
}
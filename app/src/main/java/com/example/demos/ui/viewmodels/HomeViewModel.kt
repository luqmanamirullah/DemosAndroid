package com.example.demos.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demos.models.news.NewsLists
import com.example.demos.models.news.NewsType
import com.example.demos.models.trending.TrendingLists
import com.example.demos.repository.HomeRepository
import com.example.demos.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response


class HomeViewModel(
    val homeRepository: HomeRepository
): ViewModel() {

    val trends: MutableLiveData<Resource<TrendingLists>> = MutableLiveData();
    val news: MutableLiveData<Resource<NewsLists>> = MutableLiveData()
    val newsType: MutableLiveData<Resource<NewsType>> = MutableLiveData()
    val searchMatch: MutableLiveData<Resource<NewsLists>> = MutableLiveData()
    init {
        getTrendingLists()
        getNews(null)
        getNewsType()
    }

    fun getTrendingLists() = viewModelScope.launch {
        trends.postValue(Resource.Loading())
        val response = homeRepository.getTrendLists()
        trends.postValue(handleTrends(response))
    }

    fun getNews(type: String?) = viewModelScope.launch {
        news.postValue(Resource.Loading())
        val response = homeRepository.getNews(type)
        news.postValue(handelNews(response))
    }

    fun getNewsType() = viewModelScope.launch {
        newsType.postValue(Resource.Loading())
        val response = homeRepository.getNewsType()
        newsType.postValue(handleNewsType(response))
    }

    fun search(searchQuery: String) = viewModelScope.launch {
        searchMatch.postValue(Resource.Loading())
        val response = homeRepository.search(searchQuery)
        searchMatch.postValue(handleSearch(response))
    }

    private fun handleTrends(response: Response<TrendingLists>): Resource<TrendingLists>{
        if (response.isSuccessful){
            response.body()?.let { res ->
                return Resource.Success(res)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handelNews(response: Response<NewsLists>): Resource<NewsLists>{
        if (response.isSuccessful){
            response.body()?.let { res ->
                return Resource.Success(res)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleNewsType(response: Response<NewsType>): Resource<NewsType>{
        if (response.isSuccessful){
            response.body()?.let { res ->
                return Resource.Success(res)
            }
        }
        return Resource.Error(response.message())
    }
    private fun handleSearch(response: Response<NewsLists>): Resource<NewsLists>{
        if (response.isSuccessful){
            response.body()?.let { res ->
                return Resource.Success(res)
            }
        }
        return Resource.Error(response.message())
    }
}
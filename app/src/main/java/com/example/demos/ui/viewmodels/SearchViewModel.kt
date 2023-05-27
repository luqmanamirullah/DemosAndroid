package com.example.demos.ui.viewmodels

import android.icu.text.CaseMap.Title
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demos.models.newsFromInternet.NewsInternet
import com.example.demos.repository.SearchRepository
import com.example.demos.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response


class SearchViewModel(val searchRepository: SearchRepository): ViewModel() {
    val searchNews: MutableLiveData<Resource<NewsInternet>> = MutableLiveData()

    fun searchNews(query: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = searchRepository.searchNews(query)
        searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleSearchNewsResponse(response: Response<NewsInternet>): Resource<NewsInternet>{
        if (response.isSuccessful){
            response.body()?.let { res ->
                return Resource.Success(res)
            }
        }
        return Resource.Error(response.message())
    }

}
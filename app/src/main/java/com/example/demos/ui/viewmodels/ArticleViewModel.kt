package com.example.demos.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demos.models.news.Article
import com.example.demos.models.news.ArticleData
import com.example.demos.models.news.NewsLists
import com.example.demos.repository.ArticleRepository
import com.example.demos.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class ArticleViewModel(
    val articleRepository: ArticleRepository
): ViewModel() {
    val article: MutableLiveData<Resource<ArticleData>> = MutableLiveData()

    fun getNewsDetail(newsId: Int) = viewModelScope.launch {
        article.postValue(Resource.Loading())
        val response = articleRepository.getNewsDetails(newsId)
        article.postValue(handleNewsDetails(response))
    }

    private fun handleNewsDetails(response: Response<ArticleData>): Resource<ArticleData>{
        if (response.isSuccessful){
            response.body()?.let { res ->
                return Resource.Success(res)
            }
        }
        return Resource.Error(response.message())
    }
}
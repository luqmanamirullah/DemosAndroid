package com.example.demos.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demos.models.login.Login
import com.example.demos.models.trending.TrendingLists
import com.example.demos.repository.LoginRepository
import com.example.demos.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel(
    val loginRepository: LoginRepository
): ViewModel() {

    val loginResult: MutableLiveData<Resource<Login>> = MutableLiveData();

    suspend fun login(email: String, pwd: String){
        loginResult.postValue(Resource.Loading())
        val response = loginRepository.login(email, pwd)
        loginResult.postValue(handleLogin(response))
    }

    private fun handleLogin(response: Response<Login>): Resource<Login>{
        if (response.isSuccessful){
            response.body()?.let { res ->
                return  Resource.Success(res)
            }
        }
        return Resource.Error(response.message())
    }

}
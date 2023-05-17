package com.example.demos.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.demos.models.Logout
import com.example.demos.repository.LogoutRepository
import com.example.demos.utils.Resource
import retrofit2.Response

class LogoutViewModel(
    val logoutRepository: LogoutRepository
): ViewModel() {

    val logoutResult: MutableLiveData<Resource<Logout>> = MutableLiveData()

    suspend fun logout(token: String){
        logoutResult.postValue(Resource.Loading())
        val response = logoutRepository.logout(token)
        logoutResult.postValue(handleLogout(response))
    }

    private fun handleLogout(response: Response<Logout>): Resource<Logout>{
        if (response.isSuccessful){
            response.body()?.let { res ->
                return Resource.Success(res)
            }
        }

        return Resource.Error(response.message())
    }
}
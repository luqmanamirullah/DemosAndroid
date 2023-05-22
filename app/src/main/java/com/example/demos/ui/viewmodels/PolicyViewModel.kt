package com.example.demos.ui.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demos.models.opinion.CreateOpinion
import com.example.demos.models.opinion.Opinions
import com.example.demos.models.policy.DetailsPolicyLists
import com.example.demos.models.policy.Policies
import com.example.demos.models.policy.Policy
import com.example.demos.models.policy.PolicyFile
import com.example.demos.repository.PolicyRepository
import com.example.demos.utils.Resource
import com.example.demos.utils.SessionManager
import kotlinx.coroutines.launch
import retrofit2.Response


class PolicyViewModel(
    val policyRepository: PolicyRepository,
    application: Application
): AndroidViewModel(application) {

    val policies: MutableLiveData<Resource<Policies>> = MutableLiveData()
    val detailsPolicy: MutableLiveData<Resource<DetailsPolicyLists>> = MutableLiveData()
    val file: MutableLiveData<Resource<PolicyFile>> = MutableLiveData()
    val opinions: MutableLiveData<Resource<Opinions>> = MutableLiveData()
    val createOpinionsResult: MutableLiveData<Resource<CreateOpinion>> = MutableLiveData()
    init {
        viewModelScope.launch {
            SessionManager.getToken(application.applicationContext)?.let {
                getPolicies(it)
            }
        }
    }


    suspend fun getPolicies(token: String){
        policies.postValue(Resource.Loading())
        val response = policyRepository.getPolicy(token)
        policies.postValue(handleGetPolicies(response))

    }

    suspend fun getDetailsPolicy(id: Int, token: String){
        detailsPolicy.postValue(Resource.Loading())
        val response = policyRepository.getDetailsPolicy(id, token)
        detailsPolicy.postValue(handleGetDetailsPolicy(response))
    }

    suspend fun getPolicyFile(id: Int, token: String){
        file.postValue(Resource.Loading())
        val response = policyRepository.getPolicyFile(id, token)
        file.postValue(handleGetPolicyFile(response))
    }

    suspend fun getOpinions(policyId: Int, token: String){
        opinions.postValue(Resource.Loading())
        val response = policyRepository.getOpinions(policyId, token)
        opinions.postValue(handleGetOpinions(response))
    }

    suspend fun createOpinion(policyId: Int, token: String, content: String, isAgree: Int){
        createOpinionsResult.postValue(Resource.Loading())
        val response = policyRepository.createOpinion(policyId, token, content, isAgree)
        createOpinionsResult.postValue(handleCreateOpinion(response))
    }

    private fun handleGetPolicies(response: Response<Policies>): Resource<Policies>{
        if (response.isSuccessful){
            response.body()?.let { res ->
                return Resource.Success(res)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleGetDetailsPolicy(response: Response<DetailsPolicyLists>): Resource<DetailsPolicyLists>{
        if (response.isSuccessful){
            response.body()?.let { res ->
                return Resource.Success(res)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleGetPolicyFile(response: Response<PolicyFile>): Resource<PolicyFile>{
        if (response.isSuccessful){
            response.body()?.let { res ->
                return  Resource.Success(res)
            }
        }

        return Resource.Error(response.message())
    }

    private fun handleGetOpinions(response: Response<Opinions>): Resource<Opinions>{
        if (response.isSuccessful){
            response.body()?.let {res ->
                return Resource.Success(res)
            }
        }

        return Resource.Error(response.message())
    }

    private fun handleCreateOpinion(response: Response<CreateOpinion>): Resource<CreateOpinion>{
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
}
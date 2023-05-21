package com.example.demos.repository

import com.example.demos.api.RetrofitInstance
import com.example.demos.models.opinion.OpinionRequest

class PolicyRepository {
    suspend fun getPolicy(token: String) =
        RetrofitInstance.api.policy(token)

    suspend fun getDetailsPolicy(id: Int, token: String) =
        RetrofitInstance.api.policyDetails(id, token)

    suspend fun getPolicyFile(id: Int, token: String) =
        RetrofitInstance.api.policyFile(id, token)

    suspend fun getOpinions(policyId: Int, token: String) =
        RetrofitInstance.api.opinions(policyId, token)

    suspend fun createOpinion(policyId: Int, token: String, content: String, isAgree: Int) =
        RetrofitInstance.api.createOpinion(policyId, token, OpinionRequest(content, isAgree))
}
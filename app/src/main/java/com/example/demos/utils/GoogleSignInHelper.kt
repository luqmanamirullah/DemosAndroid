package com.example.demos.utils

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class GoogleSignInHelper(private val activity: Activity, private val requestCode: Int) {
    private val googleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        GoogleSignIn.getClient(activity, gso)
    }

    fun login(){
        val signIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signIntent, requestCode)
    }

    fun handleSignInResult(data: Intent?): GoogleSignInAccount?{
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        return try {
            task.getResult(ApiException::class.java)
        } catch (e: ApiException){
            Log.e(Constants.GOOGLE_SIGN_IN, "Google sign in failed", e)
            null
        }
    }
}
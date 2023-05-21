package com.example.demos.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.demos.R
import com.example.demos.models.login.User

object SessionManager {

    fun getCurentUser(context: Context): User{
        return  User(
            id = getInteger(context, Constants.USER_ID),
            user_name = getString(context, Constants.USER_NAME),
            user_email = getString(context, Constants.USER_EMAIL),
            user_verify = getInteger(context, Constants.USER_VERIFY),
            user_photo = getString(context, Constants.USER_PICTURE),
            created_at = getString(context, Constants.USER_CREATED_AT),
            updated_at = getString(context, Constants.USER_UPDATED_AT)
        )
    }




    fun saveAuthToken(context: Context, token: String){
        saveString(context, Constants.USER_TOKEN, token)
    }

    fun saveCurrentUser(context: Context, user: User){
        saveInteger(context, Constants.USER_NAME, user.id)
        saveString(context, Constants.USER_NAME, user.user_name)
        saveString(context, Constants.USER_EMAIL, user.user_email)
        saveInteger(context, Constants.USER_VERIFY, user.user_verify)
        saveString(context, Constants.USER_PICTURE, user.user_photo)
        saveString(context, Constants.USER_CREATED_AT, user.created_at)
        user.updated_at?.let {
            saveString(context, Constants.USER_UPDATED_AT, user.updated_at)
        }
    }

    fun getToken(context: Context): String? {
        return  getString(context, Constants.USER_TOKEN)
    }

    private fun saveInteger(context: Context, key: String, value: Int){
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name),
                Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    private fun saveString(context: Context, key: String, value: String?){
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name),
            Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun getString(context: Context, key: String): String?{
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name),
            Context.MODE_PRIVATE)
        return prefs.getString(key, null)
    }

    private fun getInteger(context: Context, key: String): Int{
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name),
                Context.MODE_PRIVATE)
        return prefs.getInt(key, -1)
    }

    fun clearData(context: Context){
        val editor =
            context.getSharedPreferences(context.getString(R.string.app_name),
            Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
    }
}
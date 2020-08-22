package com.dayakar.ktorrestapi.network

import android.content.Context
import android.content.SharedPreferences
import com.dayakar.simplenote.R
import com.dayakar.simplenote.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Cookie
import java.lang.reflect.Type

class SessionManager ( context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_SESSION="user_session"
        const val CURRENT_USER="currentUser"
    }


    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }
    fun <T> saveCoockies( list: List<T>?) {
        val gson = Gson()
        val json = gson.toJson(list)
        set(USER_SESSION, json)
    }

    operator fun set(key: String?, value: String?) {
        val cookieEditor=prefs.edit()
        cookieEditor.putString(key, value)
        cookieEditor.apply()

    }

    fun saveCurrentUserInfo(user:User){
        val userEditor=prefs.edit()
        val userDetails=Gson().toJson(user)
        userEditor.putString(CURRENT_USER,userDetails)
        userEditor.apply()
    }

    fun getCurrentUser():User?{
        val userDetails=prefs.getString(CURRENT_USER,null)
        if (userDetails!=null){
            return Gson().fromJson(userDetails,User::class.java)

        }
        return null
    }

    fun getCookies():List<Cookie>?{
        var arrayItems:List<Cookie>?=null
       val  serializedObject = prefs.getString(USER_SESSION, null)
        if (serializedObject != null) {
            val gson =  Gson()
            val type: Type = object : TypeToken<List<Cookie?>?>() {}.type
            arrayItems = gson.fromJson<List<Cookie>>(serializedObject, type)
        }
        return arrayItems
    }
}
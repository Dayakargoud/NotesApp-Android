package com.dayakar.simplenote.network

import android.content.Context
import com.dayakar.ktorrestapi.network.SessionManager
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import java.util.*
import kotlin.collections.ArrayList

class UserCookieJar(context: Context): CookieJar {
    private var cookies: List<Cookie>? = null
   private var sessionManager:SessionManager= SessionManager(context)
    
    override fun saveFromResponse(
        url: HttpUrl,
        cookies: List<Cookie>
    ) {
        if (url.encodedPath.endsWith("login") || url.encodedPath.endsWith("create")) {
            this.cookies = ArrayList(cookies)
            sessionManager.saveCoockies(cookies)
        }
    }
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return if (!url.encodedPath.endsWith("login") && !url.encodedPath.endsWith("create")) {
            if(cookies==null){
             cookies=sessionManager.getCookies()
            }
            cookies!!
        } else Collections.emptyList()
    }
}
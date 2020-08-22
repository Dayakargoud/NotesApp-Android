package com.dayakar.simplenote.di

import android.content.Context
import com.dayakar.simplenote.network.AuthInterceptor
import com.dayakar.simplenote.network.NotesAPI
import com.dayakar.simplenote.network.UserCookieJar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val URL_LINK =
    "http://192.168.43.52:8080/"

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()

    }
    @Singleton
    @Provides
    fun provideRetrofit(
        gson: Gson,
        okHttpClient: OkHttpClient
    ): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(URL_LINK)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)

    }
    @Singleton
    @Provides
    fun provideNoteApi(retrofit: Retrofit.Builder): NotesAPI {
        return retrofit.build()
            .create(NotesAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideCookies(@ApplicationContext context: Context): UserCookieJar {
        return UserCookieJar(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(userCookieJar: UserCookieJar,authInterceptor: AuthInterceptor,httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient().newBuilder().cookieJar(userCookieJar)
            .addInterceptor(authInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .retryOnConnectionFailure(true)
            .connectTimeout(5,TimeUnit.SECONDS)
            .writeTimeout(5,TimeUnit.SECONDS)
            .readTimeout(5,TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request: Request = chain.request().newBuilder()
                    .addHeader("Connection", "close")
                    .build()
                chain.proceed(request)
            }
            .build()

    }
    @Provides
    @Singleton
    fun provideInterceptor(@ApplicationContext context: Context): AuthInterceptor {
        return AuthInterceptor(context)
    }
    @Provides
    @Singleton
    fun provideLoggingInterceptor():HttpLoggingInterceptor{
        val loggingInterceptor=HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return loggingInterceptor
    }


}


package com.towich.cosmicintrigue.ui.util

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.towich.cosmicintrigue.data.network.ApiRoutes
import com.towich.cosmicintrigue.data.network.ApiService
import com.towich.cosmicintrigue.data.network.StompController
import com.towich.cosmicintrigue.data.repository.MainRepository
import com.towich.cosmicintrigue.data.repository.MainRepositoryImpl
import com.towich.cosmicintrigue.data.source.Constants
import com.towich.cosmicintrigue.data.source.SessionStorage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

class App : Application() {
    private val gson: Gson = GsonBuilder().create()

    private val stompClient: StompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Constants.SOCKET_URL)
        .withServerHeartbeat(30000)

    private val httpLoggingInterceptor: HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClient: OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor { chain ->
                try {
                    chain.proceed(chain.request())
                } catch (e: SocketTimeoutException) {
                    // Обработка исключения
                    throw IOException("Превышено время ожидания ответа от сервера", e)
                }
            }
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

    private val retrofit: Retrofit =
        Retrofit.Builder().baseUrl(ApiRoutes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    private val apiService: ApiService =
        retrofit.create(ApiService::class.java)

    private val stompController: StompController = StompController(
        mStompClient = stompClient,
        gson = gson
    )

    private val sessionStorage: SessionStorage = SessionStorage()

    val repository: MainRepository =
        MainRepositoryImpl(
            stompController = stompController,
            apiService = apiService,
            gson = gson,
            mStompClient = stompClient,
            sessionStorage = sessionStorage
        )

    val viewModelFactory: ViewModelFactory = ViewModelFactory(repository)
}
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
import com.towich.cosmicintrigue.di.component.DaggerAppComponent
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
    val appComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}
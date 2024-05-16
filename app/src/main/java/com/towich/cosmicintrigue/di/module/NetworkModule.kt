package com.towich.cosmicintrigue.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.towich.cosmicintrigue.data.network.ApiRoutes
import com.towich.cosmicintrigue.data.network.ApiService
import com.towich.cosmicintrigue.data.network.StompController
import com.towich.cosmicintrigue.data.repository.MainRepository
import com.towich.cosmicintrigue.data.source.Constants
import com.towich.cosmicintrigue.data.source.SessionStorage
import com.towich.cosmicintrigue.di.scope.AppScope
import com.towich.cosmicintrigue.ui.util.ViewModelFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

@Module
object NetworkModule {

    @Provides
    @AppScope
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @AppScope
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @AppScope
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
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

    @Provides
    @AppScope
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        sessionStorage: SessionStorage
    ): Retrofit =
        Retrofit.Builder().baseUrl(sessionStorage.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @AppScope
    fun provideApiService(
        retrofit: Retrofit
    ): ApiService =
        retrofit.create(ApiService::class.java)


    // WebSockets

    @Provides
    @AppScope
    fun provideStompClient(
        sessionStorage: SessionStorage
    ): StompClient =
        Stomp.over(Stomp.ConnectionProvider.OKHTTP, sessionStorage.BASE_URL + Constants.SOCKET_URL)
            .withServerHeartbeat(30000)

    @Provides
    @AppScope
    fun provideStompController(
        mStompClient: StompClient,
        gson: Gson
    ): StompController = StompController(
        mStompClient = mStompClient,
        gson = gson
    )
}
package com.towich.cosmicintrigue.di.module

import android.content.Context
import com.google.gson.Gson
import com.towich.cosmicintrigue.data.network.ApiService
import com.towich.cosmicintrigue.data.network.StompController
import com.towich.cosmicintrigue.data.repository.MainRepository
import com.towich.cosmicintrigue.data.repository.MainRepositoryImpl
import com.towich.cosmicintrigue.data.source.SessionStorage
import com.towich.cosmicintrigue.di.scope.AppScope
import dagger.Module
import dagger.Provides
import ua.naiksoftware.stomp.StompClient

@Module
object AppModule {

    @Provides
    @AppScope
    fun provideSessionStorage(
        context: Context
    ): SessionStorage =
        SessionStorage(context = context)

    @Provides
    @AppScope
    fun provideMainRepositoryImpl(
        stompController: StompController,
        apiService: ApiService,
        sessionStorage: SessionStorage
    ): MainRepository = MainRepositoryImpl(
        stompController = stompController,
        apiService = apiService,
        sessionStorage = sessionStorage
    )
}
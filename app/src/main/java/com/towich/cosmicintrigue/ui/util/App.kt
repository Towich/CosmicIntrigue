package com.towich.cosmicintrigue.ui.util

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.towich.cosmicintrigue.data.repository.MainRepository
import com.towich.cosmicintrigue.data.repository.MainRepositoryImpl
import com.towich.cosmicintrigue.data.source.Constants
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import java.time.LocalDateTime

class App : Application() {
    private val gson: Gson = GsonBuilder().create()

    private val mStompClient: StompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Constants.SOCKET_URL)
        .withServerHeartbeat(30000)

    private val repository: MainRepository =
        MainRepositoryImpl(
            gson = gson,
            mStompClient = mStompClient
        )

    val viewModelFactory: ViewModelFactory = ViewModelFactory(repository)
}
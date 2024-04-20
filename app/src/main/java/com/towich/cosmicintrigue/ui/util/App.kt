package com.towich.cosmicintrigue.ui.util

import android.app.Application
import com.towich.cosmicintrigue.data.repository.MainRepository
import com.towich.cosmicintrigue.data.repository.MainRepositoryImpl

class App: Application() {
    val repository: MainRepository = MainRepositoryImpl()
    val viewModelFactory: ViewModelFactory = ViewModelFactory(repository)
}
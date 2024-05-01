package com.towich.cosmicintrigue.di.module

import com.towich.cosmicintrigue.data.repository.MainRepository
import com.towich.cosmicintrigue.di.scope.AppScope
import com.towich.cosmicintrigue.ui.util.ViewModelFactory
import dagger.Module
import dagger.Provides

@Module
object ViewModelModule {

    @Provides
    @AppScope
    fun provideViewModelFactory(
        repository: MainRepository
    ): ViewModelFactory = ViewModelFactory(
        repository = repository
    )
}
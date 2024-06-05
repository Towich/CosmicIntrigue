package com.towich.cosmicintrigue.di.component

import android.content.Context
import com.towich.cosmicintrigue.application.activity.GameActivity
import com.towich.cosmicintrigue.application.activity.MenuActivity
import com.towich.cosmicintrigue.di.module.AppModule
import com.towich.cosmicintrigue.di.module.NetworkModule
import com.towich.cosmicintrigue.di.module.ViewModelModule
import com.towich.cosmicintrigue.di.scope.AppScope
import com.towich.cosmicintrigue.ui.util.ViewModelFactory
import dagger.BindsInstance
import dagger.Component


@AppScope
@Component(modules = [AppModule::class, NetworkModule::class, ViewModelModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun viewModelsFactory(): ViewModelFactory

    fun inject(gameActivity: GameActivity)
    fun inject(menuActivity: MenuActivity)

    fun menuComponent(): MenuComponent
    fun gameComponent(): GameComponent
//    fun activityComponent(): ActivityComponent.Factory
}
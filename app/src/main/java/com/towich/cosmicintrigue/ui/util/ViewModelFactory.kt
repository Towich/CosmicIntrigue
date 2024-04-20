package com.towich.cosmicintrigue.ui.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.towich.cosmicintrigue.data.repository.MainRepository
import com.towich.cosmicintrigue.ui.viewmodel.LoginViewModel
import com.towich.cosmicintrigue.ui.viewmodel.MapViewModel


class ViewModelFactory(
    private val repository: MainRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MapViewModel::class.java) -> {
                MapViewModel(
                    repository = repository
                ) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
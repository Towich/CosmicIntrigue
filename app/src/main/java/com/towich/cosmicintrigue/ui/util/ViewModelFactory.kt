package com.towich.cosmicintrigue.ui.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.towich.cosmicintrigue.data.repository.MainRepository
import com.towich.cosmicintrigue.ui.viewmodel.MapViewModel


@Suppress("UNCHECKED_CAST")
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

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
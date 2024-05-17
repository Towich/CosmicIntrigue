package com.towich.cosmicintrigue.ui.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.towich.cosmicintrigue.data.repository.MainRepository
import com.towich.cosmicintrigue.ui.viewmodel.DeathViewModel
import com.towich.cosmicintrigue.ui.viewmodel.FinalViewModel
import com.towich.cosmicintrigue.ui.viewmodel.LoginViewModel
import com.towich.cosmicintrigue.ui.viewmodel.MapViewModel
import com.towich.cosmicintrigue.ui.viewmodel.RoleViewModel
import com.towich.cosmicintrigue.ui.viewmodel.TaskViewModel
import com.towich.cosmicintrigue.ui.viewmodel.VoteViewModel
import com.towich.cosmicintrigue.ui.viewmodel.WRoomViewModel


@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: MainRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DeathViewModel::class.java) -> {
                DeathViewModel() as T
            }
            modelClass.isAssignableFrom(FinalViewModel::class.java) -> {
                FinalViewModel(
                    repository = repository
                ) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(
                    repository = repository
                ) as T
            }
            modelClass.isAssignableFrom(MapViewModel::class.java) -> {
                MapViewModel(
                    repository = repository
                ) as T
            }
            modelClass.isAssignableFrom(RoleViewModel::class.java) -> {
                RoleViewModel(
                    repository = repository
                ) as T
            }
            modelClass.isAssignableFrom(TaskViewModel::class.java) -> {
                TaskViewModel(
                    repository = repository
                ) as T
            }
            modelClass.isAssignableFrom(VoteViewModel::class.java) -> {
                VoteViewModel(
                    repository = repository
                ) as T
            }
            modelClass.isAssignableFrom(WRoomViewModel::class.java) -> {
                WRoomViewModel(
                    repository = repository
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
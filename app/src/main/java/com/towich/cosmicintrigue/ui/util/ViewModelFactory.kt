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
                FinalViewModel() as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel() as T
            }
            modelClass.isAssignableFrom(MapViewModel::class.java) -> {
                MapViewModel(
                    repository = repository
                ) as T
            }
            modelClass.isAssignableFrom(RoleViewModel::class.java) -> {
                RoleViewModel() as T
            }
            modelClass.isAssignableFrom(TaskViewModel::class.java) -> {
                TaskViewModel() as T
            }
            modelClass.isAssignableFrom(VoteViewModel::class.java) -> {
                VoteViewModel() as T
            }
            modelClass.isAssignableFrom(WRoomViewModel::class.java) -> {
                WRoomViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
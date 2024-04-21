package com.towich.cosmicintrigue.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.data.model.ReadyPlayer
import com.towich.cosmicintrigue.data.repository.MainRepository

class WRoomViewModel(
    private val repository: MainRepository
): ViewModel() {
    //TODO("Список ждущих игроков websocket")
    val players : MutableLiveData<List<Player>> by lazy {
        MutableLiveData<List<Player>>()
    }
    //TODO("Отправка готовности websocket")
    val ready: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun getId(): Long {
        //TODO("получение id без запроса")
        return repository.getCurrentPlayer()?.id ?: -1
    }
    fun saveRole(role:Boolean){
        // TODO Сохранение роли пользователя в репозитории после получения без запроса
    }
}
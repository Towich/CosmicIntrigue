package com.towich.cosmicintrigue.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.towich.cosmicintrigue.data.model.ReadyPlayer
import com.towich.cosmicintrigue.data.repository.MainRepository

class WRoomViewModel(
    private val repository: MainRepository
): ViewModel() {
    var players = MutableLiveData<List<ReadyPlayer>>() //TODO("Список ждущих игроков websocket")
    var ready = MutableLiveData<Boolean>() //TODO("отпраление на сервер того готов игрок или нет websocket/post запрос")
    var status = MutableLiveData<String>() //TODO("получение статусу комнаты websocket")
    fun setStartCallback(a:()->Unit){
        TODO("навигации в начало игры " +
                "websocket")
    }
    fun getId(): Long {
//        TODO("получение id " +
//                "без запроса")
        return repository.getCurrentPlayer()?.id ?: -1
    }

}
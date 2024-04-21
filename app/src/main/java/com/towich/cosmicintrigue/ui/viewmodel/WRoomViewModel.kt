package com.towich.cosmicintrigue.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.towich.cosmicintrigue.data.model.ReadyPlayer

class WRoomViewModel(): ViewModel() {
    var players = MutableLiveData<List<ReadyPlayer>>() //TODO("Список ждущих игроков websocket")
    var ready = MutableLiveData<Boolean>() //TODO("отпраление на сервер того готов игрок или нет websocket/post запрос")
    //var status = MutableLiveData<String>() //TODO("получение статусу комнаты websocket")
    fun setStartCallback(a:()->Unit){
        TODO("навигации в начало игры " +
                "websocket")
    }
    fun getId():Int{
        TODO("получение id " +
                "без запроса")
    }

}
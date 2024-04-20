package com.towich.cosmicintrigue.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.towich.cosmicintrigue.data.model.ReadyPlayer

class WRoomViewModel(): ViewModel() {
    fun getPlayers(): List<ReadyPlayer>{
        TODO("Список ждущих игроков")
    }
    fun sendReady(ready:Boolean){
        TODO("отпраление того готов игрок или нет")
    }
    fun SetWaitCallback(a:()->Unit){
        TODO("проброс навигации перехода в игру")
    }

}
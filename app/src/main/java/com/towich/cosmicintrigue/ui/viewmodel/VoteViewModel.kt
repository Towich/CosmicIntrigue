package com.towich.cosmicintrigue.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.towich.cosmicintrigue.data.model.Player

class VoteViewModel(): ViewModel() {
    fun getPlayers():List<Player>{
        TODO("Список игроков ичаствующих в голосовании" +
                "запрос get")
    }
    fun vote(id: Int?){
        TODO("Голосование за игрока по id, null - пропуск голосования" +
                "запрос post")
    }
    fun getVotes(callback: (List<Pair<Int?,Int>>) -> Unit){
        TODO("Получение резудьтатов голосования " +
                "первый - id" +
                "второй - кол-во голосов" +
                "запрос topic")
    }
    fun getUserId():Int{
        TODO("Возвращает id пользователя")
    }
}
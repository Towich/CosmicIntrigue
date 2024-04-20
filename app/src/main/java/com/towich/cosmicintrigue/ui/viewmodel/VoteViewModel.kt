package com.towich.cosmicintrigue.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.towich.cosmicintrigue.data.model.Player

class VoteViewModel(): ViewModel() {
    fun getPlayers():List<Player>{
        TODO("Список игроков ичаствующих в голосовании")
    }
    fun vote(id: Int?){
        TODO("Голосование за игрока по id, null - пропуск голосования")
    }
    fun getVotes(callback: (List<Pair<Int,Int>>) -> Unit){
        TODO("Получение резудьтатов голосования " +
                "первый - id" +
                "второй - кол-во голосов")
    }
    fun onDeath(callback: ()->Unit){
        TODO("Переход на экран смерти")
    }
    fun OnVoteEnd(callback: ()->Unit){
        TODO("Возврат на карту после конца голосования")
    }
}
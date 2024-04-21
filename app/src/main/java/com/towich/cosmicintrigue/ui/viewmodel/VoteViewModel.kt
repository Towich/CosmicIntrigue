package com.towich.cosmicintrigue.ui.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.data.source.Constants.VOTE_TIMER_MILIS

class VoteViewModel(): ViewModel() {
    //TODO (список голосующих игроков)
    //TODO(Добавить проверку на смерть игрока)
    fun getPlayers():List<Player>{
        //return
        TODO("Список игроков ичаствующих в голосовании" +
                "запрос get")
    }
    fun sendVote(id: Long?){
        TODO("Голосование за игрока по id, " +
                "null = пропуск голосования" +
                "запрос post")
    }


    fun getVotes(callbackMain: (List<Pair<Long?,Int>>) -> Unit, callbackTick: (Long)->Unit){
        object : CountDownTimer(VOTE_TIMER_MILIS, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                callbackTick.invoke(millisUntilFinished)
            }
            override fun onFinish() {
                TODO("Получение резудьтатов голосования " +
                        "первый - id" +
                        "второй - кол-во голосов" +
                        "запрос topic")
                //callbackMain.invoke()
            }
        }.start()
    }

    fun getUserId():Long{
        TODO("Возвращает id пользователя")
    }
}
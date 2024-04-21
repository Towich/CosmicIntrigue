package com.towich.cosmicintrigue.ui.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.data.source.Constants.VOTE_TIMER_MILIS

class VoteViewModel(): ViewModel() {
    //val votes = MutableLiveData<List<Pair<Int?,Int>>>()
    //val player = MutableLiveData<List<Pair<Int?,Int>>>()
    fun getPlayers():List<Player>{
        TODO("Список игроков ичаствующих в голосовании" +
                "запрос get")
    }
    fun setVote(id: Int?){
        TODO("Голосование за игрока по id, " +
                "null = пропуск голосования" +
                "запрос post")
    }
    fun getVotes(callbackMain: (List<Pair<Int?,Int>>) -> Unit, callbackTick: (Long)->Unit){
        object : CountDownTimer(VOTE_TIMER_MILIS, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                callbackTick.invoke(millisUntilFinished)
            }
            override fun onFinish() {
                //callbackMain.invoke()
                TODO("Получение резудьтатов голосования " +
                        "первый - id" +
                        "второй - кол-во голосов" +
                        "запрос topic")
            }
        }.start()
    }
    fun getUserId():Int{
        TODO("Возвращает id пользователя")
    }
    public fun setDeathCallback(callback:()->Unit){
        TODO("проброс навигации если был выбран " +
                "без запроса")
    }
}
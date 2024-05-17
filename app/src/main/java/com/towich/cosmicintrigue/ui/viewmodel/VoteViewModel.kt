package com.towich.cosmicintrigue.ui.viewmodel

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.data.source.Constants.VOTE_TIMER_MILIS

class VoteViewModel() : ViewModel() {
    //TODO (список голосующих игроков)
    //TODO(Добавить проверку на смерть игрока)
    fun getPlayers(): List<Player> {
        return arrayListOf(
            Player(1, "user", true, true),
            Player(2, "player1", true, false),
            Player(3, "player2", true, false)
        )
    }

    fun sendVote(id: Long?) {
        Log.d("voted for ", id.toString())
        /*
        TODO("Голосование за игрока по id, " +
                "null = пропуск голосования" +
                "запрос post")
         */
    }


    fun getVotes(callbackMain: (List<Pair<Long?, Int>>) -> Unit, callbackTick: (Long) -> Unit) {
        object : CountDownTimer(VOTE_TIMER_MILIS, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                callbackTick.invoke(millisUntilFinished)
            }

            override fun onFinish() {

                callbackMain.invoke(
                    arrayListOf(Pair(null, 1), Pair(1, 0), Pair(2, 1), Pair(3, 1))
                    /*TODO("Получение резудьтатов голосования " +
                            "первый - id" +
                            "второй - кол-во голосов" +
                            "запрос topic")*/
                )
            }
        }.start()
    }

    fun getUserId(): Long {
        return 1
        //TODO("Возвращает id пользователя")
    }
}
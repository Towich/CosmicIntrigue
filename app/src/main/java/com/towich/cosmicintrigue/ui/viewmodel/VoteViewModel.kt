package com.towich.cosmicintrigue.ui.viewmodel

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.data.network.ApiResult
import com.towich.cosmicintrigue.data.repository.MainRepository
import com.towich.cosmicintrigue.data.source.Constants.VOTE_TIMER_MILIS
import kotlinx.coroutines.launch

class VoteViewModel(
    private val repository: MainRepository
) : ViewModel() {

    val playersToVote: MutableLiveData<List<Player>> by lazy {
        MutableLiveData<List<Player>>()
    }

    fun sendPlayerModelToKick(
        id: Long
    ){
        repository.sendPlayerModelToKick(Player(id = id, login = ""))
    }

    fun subscribeVoteTopic(
        onReceivedPlayerToKick: (playerToKick: Player) -> Unit
    ) {
        repository.subscribeVoteTopic(onReceivedPlayerToKick)
    }

    fun getAliveUsers() {
        viewModelScope.launch {
            when (val result = repository.getAlivePlayers()
            ) {
                is ApiResult.Success -> {
                    playersToVote.value = result.data
                    for (player in result.data) {
                        Log.i("VoteViewModel", player.login)
                    }
                }

                is ApiResult.Error -> {
                    Log.e("VoteViewModel", result.error)
                }
            }
        }

    }

    fun getVotes(
        onFinished: (List<Pair<Long?, Int>>) -> Unit,
        onTick: (Long) -> Unit
    ) {
        object : CountDownTimer(VOTE_TIMER_MILIS, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                onTick.invoke(millisUntilFinished)
            }
            override fun onFinish() {
//                onFinished.invoke(
//                    arrayListOf(Pair(null, 1), Pair(1, 0), Pair(2, 1), Pair(3, 1))
//                    /*TODO("Получение резудьтатов голосования " +
//                            "первый - id" +
//                            "второй - кол-во голосов" +
//                            "запрос topic")*/
//                )
            }
        }.start()
    }

    fun getUserId(): Long {
        return repository.getCurrentPlayer()?.id ?: -1
    }
}
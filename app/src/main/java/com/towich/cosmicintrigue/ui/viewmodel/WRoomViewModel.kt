package com.towich.cosmicintrigue.ui.viewmodel

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.data.model.ReadyPlayer
import com.towich.cosmicintrigue.data.repository.MainRepository
import com.towich.cosmicintrigue.data.source.Constants

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
    init{
        object : CountDownTimer(Constants.TASK_TIMER_MILIS, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                players.value = arrayListOf(Player(1,"user",false,false),
                    Player(2,"player1",true,false),
                    Player(3,"player2",true,false))
            }
        }.start()
        //ready.observe(viewLifecycleOwner){}
    }
    fun getId(): Long {
        //TODO("получение id без запроса")
        //return repository.getCurrentPlayer()?.id ?: -1
        return 1
    }
    fun saveRole(role:Boolean){
        Log.d("role saved",role.toString())
        // TODO Сохранение роли пользователя в репозитории после получения без запроса
    }
}
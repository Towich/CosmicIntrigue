package com.towich.cosmicintrigue.ui.viewmodel

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.data.source.Constants.TASK_TIMER_MILIS

class TaskViewModel(): ViewModel() {
    lateinit var t:CountDownTimer
    //TODO("Список игроков websocket")
    val players : MutableLiveData<List<Player>> by lazy {
        MutableLiveData<List<Player>>()
    }
    fun onCompleteTask(){
        Log.d("task","completed")
        //TODO("Отправка данных о завершении post запрос")
    }
    fun onDestroy(){
        t.cancel()
    }
    fun setOnCompleteCallback(callbackMain:()->Unit,callbackTick:(Long)->Unit){//изменение кнопки по завершению задачи
         t = object : CountDownTimer(TASK_TIMER_MILIS, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                callbackTick.invoke(millisUntilFinished)
            }
            override fun onFinish() {
                callbackMain.invoke()
            }
        }.start()
    }
}
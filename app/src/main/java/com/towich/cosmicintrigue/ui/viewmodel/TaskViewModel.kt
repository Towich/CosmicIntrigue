package com.towich.cosmicintrigue.ui.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.towich.cosmicintrigue.data.source.Constants.TASK_TIMER_MILIS

class TaskViewModel(): ViewModel() {
    fun interruptTask(){
        TODO("Отправка данных о прерывании" +
                "post запрос")
    }
    fun finishTask(){
        TODO("Отправка данных о завершении"+
                "post запрос")
    }
    fun setOnCompleteCallback(callbackMain:()->Unit,callbackTick:(Long)->Unit){//изменение кнопки по завершению задачи
        object : CountDownTimer(TASK_TIMER_MILIS, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                callbackTick.invoke(millisUntilFinished)
            }
            override fun onFinish() {
                callbackMain.invoke()
            }
        }.start()
    }
}
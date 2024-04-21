package com.towich.cosmicintrigue.ui.viewmodel

import androidx.lifecycle.ViewModel

class LoginViewModel(): ViewModel() {

    fun sendLogin(text: String){
        TODO("Отправка логина на сервер" +
                "post запрос")
    }
    fun setIdRecieved(callback:()->Unit){
        TODO("навиагция при получении id")
    }
}
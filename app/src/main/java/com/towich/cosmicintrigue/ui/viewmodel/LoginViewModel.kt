package com.towich.cosmicintrigue.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.data.network.ApiResult
import com.towich.cosmicintrigue.data.repository.MainRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: MainRepository
): ViewModel() {

    val currentPlayer: MutableLiveData<Player> by lazy {
        MutableLiveData<Player>()
    }

    fun sendLogin(login: String){
//        TODO("Отправка логина на сервер - post запрос")

        viewModelScope.launch {
            when (val result = repository.getUserIdByPlayerModel(Player(
                id = -1,
                login = login,
                ready = false,
                imposter = false
            ))) {
                is ApiResult.Success -> {
                    currentPlayer.value = result.data
                }

                is ApiResult.Error -> {
                    Log.e("LoginViewModel", result.error)
                }
            }
        }
    }

    fun saveCurrentPlayer(player: Player){
        repository.saveCurrentPlayer(player)
    }
}
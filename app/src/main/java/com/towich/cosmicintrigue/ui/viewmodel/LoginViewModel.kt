package com.towich.cosmicintrigue.ui.viewmodel

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.data.network.ApiResult
import com.towich.cosmicintrigue.data.repository.MainRepository
import com.towich.cosmicintrigue.data.source.Constants
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: MainRepository
): ViewModel() {

    val currentPlayer: MutableLiveData<Player> by lazy {
        MutableLiveData<Player>()
    }

    fun sendLogin(login: String){
        currentPlayer.value = Player(1,login,false,false)
    }

    fun saveCurrentPlayer(player: Player){
        Log.d("player saved",player.login)
    //repository.saveCurrentPlayer(player)
    }
}
package com.towich.cosmicintrigue.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.towich.cosmicintrigue.data.model.GameState
import com.towich.cosmicintrigue.data.repository.MainRepository

class DeathViewModel(
    private val repository: MainRepository
): ViewModel() {
    //TODO(отслеживание победителя)

    fun subscribeGameStateTopic(
        onReceivedGameState: (gameState: GameState) -> Unit
    ){
        repository.subscribeGameStateTopic(onReceivedGameState)
    }

    fun sendEmptyToGameStateTopic(){
        repository.sendEmptyToGameStateTopic()
    }

    fun setWinners(innocentsWins: Boolean){
        repository.setWinners(innocentsWins = innocentsWins)
    }
}
package com.towich.cosmicintrigue.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.towich.cosmicintrigue.data.model.GameState
import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.data.repository.MainRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class WRoomViewModel(
    private val repository: MainRepository
): ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var userTopicDisposable: Disposable? = null


    var players = MutableLiveData<List<Player>>() //TODO("Список ждущих игроков websocket")
    var ready = MutableLiveData<Boolean>() //TODO("отпраление на сервер того готов игрок или нет websocket/post запрос")
    //var status = MutableLiveData<String>() //TODO("получение статусу комнаты websocket")
    fun setStartCallback(a:()->Unit){
        TODO("навигации в начало игры " +
                "websocket")
    }
    fun getId(): Long {
//        TODO("получение id " +
//                "без запроса")
        return repository.getCurrentPlayer()?.id ?: -1
    }

    fun subscribeUsersTopic(
        onReceivedGameState: (gameState: GameState) -> Unit
    ){
        val disposable = repository.subscribeUsersTopic(onReceivedGameState)
        userTopicDisposable = disposable
    }

    fun unsubscribeUsersTopic(){
        userTopicDisposable?.dispose()
    }

    fun sendPlayerInUsersTopic(){
        repository.sendPlayerModel(compositeDisposable)
    }

    fun toggleReadyPlayer(){
        repository.toggleReadyPlayer()
    }

    fun updateIsImposter(newIsImposter: Boolean?){
        repository.updateIsImposter(newIsImposter)
    }
}
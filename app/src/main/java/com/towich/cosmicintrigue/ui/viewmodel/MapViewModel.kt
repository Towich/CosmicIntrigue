package com.towich.cosmicintrigue.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.towich.cosmicintrigue.data.model.GameState
import com.towich.cosmicintrigue.data.model.GeoPositionModel
import com.towich.cosmicintrigue.data.model.TaskGeoPositionModel
import com.towich.cosmicintrigue.data.network.ApiResult
import com.towich.cosmicintrigue.data.repository.MainRepository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class MapViewModel(
    private val repository: MainRepository
): ViewModel() {
    val currentTaskMarks: MutableLiveData<List<TaskGeoPositionModel>> by lazy {
        MutableLiveData<List<TaskGeoPositionModel>>()
    }

    val countCurrTaskMarks: MutableLiveData<Int> = repository.getCountCurrTaskCount()
    val totalTaskMarks: MutableLiveData<Int> = repository.getTotalTaskCount()

    fun getStartTaskMarks() {
        viewModelScope.launch {
            when (val result = repository.getStartTaskMarks()) {
                is ApiResult.Success -> {
                    currentTaskMarks.value = result.data
                    countCurrTaskMarks.value = currentTaskMarks.value?.count { it.completed == false }
                }

                is ApiResult.Error -> {
                    Log.e("MapViewModel", result.error)
                }

                else -> {}
            }
        }

    }

    fun subscribeGeoPosTopic(
        onReceivedGeoPosition: (geoPosition: GeoPositionModel) -> Unit
    ){
        repository.subscribeGeoPosTopic(onReceivedGeoPosition)
    }


    fun sendGeoPosition(geoPosition: GeoPositionModel) {
        repository.sendGeoPosition(geoPosition)
    }

    fun subscribeCoordinatesTopic(
        onReceivedCoordinatesList: (listOfTasksGeoPositions: List<TaskGeoPositionModel>) -> Unit
    ){
        repository.subscribeCoordinatesTopic(onReceivedCoordinatesList)
    }


    fun sendTaskGeoPositionModel(taskGeoPositionModel: TaskGeoPositionModel) {
        repository.sendTaskGeoPositionModel(taskGeoPositionModel)
    }

    fun getPlayerId(): Long? {
        return repository.getCurrentPlayer()?.id
    }

    fun setCurrTaskId(id: Long){
        repository.setCurrTaskId(id)
    }

    fun getIsImposter(): Boolean? {
        return repository.getCurrentPlayer()?.isImposter
    }

    fun setCurrPlayerIdToKill(id: Long?){
        repository.setCurrPlayerIdToKill(id)
    }
    fun getCurrPlayerIdToKill(): Long? {
        return repository.getCurrPlayerIdToKill()
    }

    fun subscribeToServerStatus(
        compositeDisposable: CompositeDisposable?,
        onOpened: () -> Unit,
        onError: (exception: Exception) -> Unit,
        onFailedServerHeartbeat: () -> Unit,
        onClosed: () -> Unit
    ){
        repository.initGeoPositionsStompClient(
            compositeDisposable = compositeDisposable,
            onOpened = onOpened,
            onError = onError,
            onFailedServerHeartbeat = onFailedServerHeartbeat,
            onClosed = onClosed
        )
    }

    fun sendTask(){
        repository.sendTaskGeoPositionModel(TaskGeoPositionModel(id = -1, latitude = 0.0, longitude = 0.0, completed = false))
    }

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

    fun getCurrentGameState(): GameState {
        return repository.getCurrentGameState()
    }

    override fun onCleared() {
        super.onCleared()
    }

}
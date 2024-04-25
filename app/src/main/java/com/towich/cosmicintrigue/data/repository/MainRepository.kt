package com.towich.cosmicintrigue.data.repository

import androidx.lifecycle.MutableLiveData
import com.towich.cosmicintrigue.data.model.GameState
import com.towich.cosmicintrigue.data.model.GeoPositionModel
import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.data.model.TaskGeoPositionModel
import com.towich.cosmicintrigue.data.network.ApiResult
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ua.naiksoftware.stomp.StompClient

interface MainRepository {
    fun sendGeoPosition(compositeDisposable: CompositeDisposable, geoPositionModel: GeoPositionModel)
    fun sendTaskGeoPositionModel(compositeDisposable: CompositeDisposable, taskGeoPositionModel: TaskGeoPositionModel)
    fun sendPlayerModel(compositeDisposable: CompositeDisposable, playerModel: Player)

    fun initGeoPositionsStompClient(
        compositeDisposable: CompositeDisposable
    )
    fun subscribeGeoPosTopic(
        onReceivedGeoPosition: (geoPosition: GeoPositionModel) -> Unit
    ): Disposable

    fun subscribeCoordinatesTopic(
        onReceivedCoordinatesList: (listOfTasksGeoPositions: List<TaskGeoPositionModel>) -> Unit
    ): Disposable

    fun subscribeUsersTopic(
        onReceivedGameState: (gameState: GameState) -> Unit
    ): Disposable

    suspend fun getStartTaskMarks(): ApiResult<List<TaskGeoPositionModel>>
    suspend fun getUserIdByPlayerModel(playerModel: Player): ApiResult<Player>

    fun saveCurrentPlayer(player: Player)

    fun getCurrentPlayer(): Player?

    fun toggleReadyPlayer()
    fun updateIsImposter(newIsImposter: Boolean?)

    fun setCurrTaskId(id: Long)
    fun getCurrTaskId(): Long

    fun setTotalTaskCount(tasks: Int)
    fun getTotalTaskCount(): MutableLiveData<Int>

    fun setCountCurrTaskCount(tasks: Int)
    fun getCountCurrTaskCount(): MutableLiveData<Int>
}
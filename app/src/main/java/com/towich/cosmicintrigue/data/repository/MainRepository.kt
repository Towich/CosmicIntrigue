package com.towich.cosmicintrigue.data.repository

import androidx.lifecycle.MutableLiveData
import com.towich.cosmicintrigue.data.model.GameState
import com.towich.cosmicintrigue.data.model.GeoPositionModel
import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.data.model.TaskGeoPositionModel
import com.towich.cosmicintrigue.data.network.ApiResult
import io.reactivex.disposables.CompositeDisposable

interface MainRepository {
    fun initGeoPositionsStompClient(
        compositeDisposable: CompositeDisposable?,
        onOpened: () -> Unit,
        onError: (exception: Exception) -> Unit,
        onFailedServerHeartbeat: () -> Unit,
        onClosed: () -> Unit
    )

    fun sendGeoPosition(
        geoPositionModel: GeoPositionModel
    )

    fun sendTaskGeoPositionModel(
        taskGeoPositionModel: TaskGeoPositionModel
    )

    fun sendPlayerModel()



    fun subscribeGeoPosTopic(
        onReceivedGeoPosition: (geoPosition: GeoPositionModel) -> Unit
    )

    fun subscribeCoordinatesTopic(
        onReceivedCoordinatesList: (listOfTasksGeoPositions: List<TaskGeoPositionModel>) -> Unit
    )

    fun subscribeUsersTopic(
        onReceivedGameState: (players: Array<Player>) -> Unit
    )

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

    fun setCurrPlayerIdToKill(id: Long?)
    fun getCurrPlayerIdToKill(): Long?

    fun reconnectToServer()

    // Vote topic
    fun subscribeVoteTopic(
        onReceivedPlayerToKick: (playerToKick: Player) -> Unit
    )
    fun sendPlayerModelToKick(
        playerModel: Player
    )

    suspend fun getAlivePlayers(): ApiResult<List<Player>>

    // GameState topic
    fun subscribeGameStateTopic(
        onReceivedGameState: (gameState: GameState) -> Unit
    )
    fun sendEmptyToGameStateTopic()


    // Restart server
    suspend fun restartServer()

    // Winners
    fun getWinners(): Boolean?
    fun setWinners(innocentsWins: Boolean)
}
package com.towich.cosmicintrigue.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.towich.cosmicintrigue.data.model.GeoPositionModel
import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.data.model.TaskGeoPositionModel
import com.towich.cosmicintrigue.data.network.ApiResult
import com.towich.cosmicintrigue.data.network.ApiService
import com.towich.cosmicintrigue.data.network.StompController
import com.towich.cosmicintrigue.data.source.SessionStorage
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Response
import ua.naiksoftware.stomp.StompClient

class MainRepositoryImpl(
    private val stompController: StompController,
    private val apiService: ApiService,
    private val gson: Gson,
    private val mStompClient: StompClient,
    private val sessionStorage: SessionStorage
) : MainRepository {

    override fun initGeoPositionsStompClient(
        compositeDisposable: CompositeDisposable?,
        onOpened: () -> Unit,
        onError: (exception: Exception) -> Unit,
        onFailedServerHeartbeat: () -> Unit,
        onClosed: () -> Unit
    ) {
        stompController.initGeoPositionsStompClient(
            compositeDisposable,
            onOpened,
            onError,
            onFailedServerHeartbeat,
            onClosed
        )
    }

    override fun sendGeoPosition(
        geoPositionModel: GeoPositionModel
    ) {
        stompController.sendGeoPosition(geoPositionModel)
    }

    override fun sendTaskGeoPositionModel(
        taskGeoPositionModel: TaskGeoPositionModel
    ) {
        stompController.sendTaskGeoPositionModel(taskGeoPositionModel)
    }

    override fun sendPlayerModel() {
        val player = getCurrentPlayer()
        if (player != null)
            stompController.sendPlayerModel(player)
    }


    override fun subscribeGeoPosTopic(
        onReceivedGeoPosition: (geoPosition: GeoPositionModel) -> Unit
    ) {
        stompController.subscribeGeoPosTopic(onReceivedGeoPosition)
    }

    override fun subscribeCoordinatesTopic(
        onReceivedCoordinatesList: (listOfTasksGeoPositions: List<TaskGeoPositionModel>) -> Unit
    ) {
        stompController.subscribeCoordinatesTopic(onReceivedCoordinatesList)
    }

    override fun subscribeUsersTopic(onReceivedPlayers: (players: Array<Player>) -> Unit) {
        stompController.subscribeUsersTopic(onReceivedPlayers)
    }


    override suspend fun getStartTaskMarks(): ApiResult<List<TaskGeoPositionModel>> {
        return try {
            val response: Response<List<TaskGeoPositionModel>> = apiService.getStartTaskMarks()

            if (response.isSuccessful) {
                setTotalTaskCount(response.body()?.size ?: -1)
                ApiResult.Success(response.body() ?: listOf())
            } else {
                ApiResult.Error(response.message())
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "unknown error")
        }

//        return ApiResult.Success(
//            listOf(
//                TaskGeoPositionModel(
//                    id = 11, latitude = 55.8010271, longitude = 37.8057306, completed = false
//                ),
//                TaskGeoPositionModel(
//                    id = 12, latitude = 51.8010271, longitude = 31.8057306, completed = false
//                ),
//                TaskGeoPositionModel(
//                    id = 13, latitude = 50.8010271 , longitude = 30.8057306, completed = false
//                )
//            )
//        )
    }

    override suspend fun getUserIdByPlayerModel(playerModel: Player): ApiResult<Player> {
        return try {
            val response: Response<Player> = apiService.getUserIdByPlayerModel(
//                id = playerModel.id,
//                login = playerModel.login,
//                ready = playerModel.ready,
//                isImposter = playerModel.isImposter
                playerModel
            )

            if (response.isSuccessful) {
                ApiResult.Success(response.body() ?: Player(id = null, login = "", ready = false))
            } else {
                ApiResult.Error(response.message())
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "unknown error")
        }
    }

    override fun saveCurrentPlayer(player: Player) {
        sessionStorage.currentPlayer = player
    }

    override fun getCurrentPlayer(): Player? {
        return sessionStorage.currentPlayer
    }

    override fun toggleReadyPlayer() {
        if (sessionStorage.currentPlayer != null) {
            sessionStorage.currentPlayer!!.ready = !sessionStorage.currentPlayer?.ready!!
        }
    }

    override fun updateIsImposter(newIsImposter: Boolean?) {
        sessionStorage.currentPlayer?.isImposter = newIsImposter
    }

    override fun setCurrTaskId(id: Long) {
        sessionStorage.currTaskId = id
    }

    override fun getCurrTaskId(): Long {
        return sessionStorage.currTaskId ?: -1
    }

    override fun setTotalTaskCount(tasks: Int) {
        sessionStorage.totalTaskCount.value = tasks
    }

    override fun getTotalTaskCount(): MutableLiveData<Int> {
        return sessionStorage.totalTaskCount
    }

    override fun setCountCurrTaskCount(tasks: Int) {
        sessionStorage.currCountTaskCount.value = tasks
    }

    override fun getCountCurrTaskCount(): MutableLiveData<Int> {
        return sessionStorage.currCountTaskCount
    }

    override fun setCurrPlayerIdToKill(id: Long?) {
        sessionStorage.currPlayerIdToKill = id
    }

    override fun getCurrPlayerIdToKill(): Long? {
        return sessionStorage.currPlayerIdToKill
    }

    override fun reconnectToServer() {
        stompController.reconnect()
    }

    // Vote topic
    override fun subscribeVoteTopic(
        onReceivedPlayerToKick: (playerToKick: Player) -> Unit
    ) {
        return stompController.subscribeVoteTopic(onReceivedPlayerToKick)
    }

    override fun sendPlayerModelToKick(
        playerModel: Player
    ) {
        stompController.sendPlayerModelToKick(playerModel)
    }

    override suspend fun restartServer() {
        try {
            apiService.restartServer()
        } catch (e: Exception) {
            Log.e("restartServer()", e.message.toString())
        }
    }

}
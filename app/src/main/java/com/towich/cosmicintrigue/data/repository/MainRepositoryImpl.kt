package com.towich.cosmicintrigue.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.towich.cosmicintrigue.data.model.GameState
import com.towich.cosmicintrigue.data.model.GeoPositionModel
import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.data.model.TaskGeoPositionModel
import com.towich.cosmicintrigue.data.network.ApiResult
import com.towich.cosmicintrigue.data.network.ApiService
import com.towich.cosmicintrigue.data.network.StompController
import com.towich.cosmicintrigue.data.source.Constants
import com.towich.cosmicintrigue.data.source.SessionStorage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompMessage
import kotlin.math.log

class MainRepositoryImpl(
    private val stompController: StompController,
    private val apiService: ApiService,
    private val gson: Gson,
    private val mStompClient: StompClient,
    private val sessionStorage: SessionStorage
) : MainRepository {
    override fun sendGeoPosition(
        compositeDisposable: CompositeDisposable,
        geoPositionModel: GeoPositionModel
    ) {
        compositeDisposable.add(
            mStompClient.send(Constants.CHAT_LINK_SOCKET, gson.toJson(geoPositionModel)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.d(
                            "StompClient",
                            "SEND GEOPOSITION: latitude = ${geoPositionModel.latitude}$, longitude = ${geoPositionModel.longitude}"
                        )
                    },
                    {
                        Log.e("StompClient", "Stomp error", it)
                    }
                )
        )
    }

    override fun sendTaskGeoPositionModel(
        compositeDisposable: CompositeDisposable,
        taskGeoPositionModel: TaskGeoPositionModel
    ) {
        compositeDisposable.add(
            mStompClient.send(Constants.COORDINATES_LINK_SOCKET, gson.toJson(taskGeoPositionModel)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.d(
                            "StompClient",
                            "SEND TASK GEOPOSITION: latitude = ${taskGeoPositionModel.latitude}$, longitude = ${taskGeoPositionModel.longitude}"
                        )
                    },
                    {
                        Log.e("StompClient", "Stomp error", it)
                    }
                )
        )
    }

    override fun sendPlayerModel(
        compositeDisposable: CompositeDisposable
    ) {
        val player = getCurrentPlayer()
        if(player != null)
            stompController.sendPlayerModel(compositeDisposable, player)
    }

    override fun initGeoPositionsStompClient(
        compositeDisposable: CompositeDisposable
    ) {
        stompController.initGeoPositionsStompClient(compositeDisposable)
    }

    override fun subscribeGeoPosTopic(
        onReceivedGeoPosition: (geoPosition: GeoPositionModel) -> Unit
    ): Disposable {
        return stompController.subscribeGeoPosTopic(onReceivedGeoPosition)
    }

    override fun subscribeCoordinatesTopic(
        onReceivedCoordinatesList: (listOfTasksGeoPositions: List<TaskGeoPositionModel>) -> Unit
    ): Disposable {
        return stompController.subscribeCoordinatesTopic(onReceivedCoordinatesList)
    }

    override fun subscribeUsersTopic(onReceivedGameState: (gameState: GameState) -> Unit): Disposable {
        return stompController.subscribeUsersTopic(onReceivedGameState)
    }


    override suspend fun getStartTaskMarks(): ApiResult<List<TaskGeoPositionModel>> {
        return try {
            val response: Response<List<TaskGeoPositionModel>> = apiService.getStartTaskMarks()

            if(response.isSuccessful){
                setTotalTaskCount(response.body()?.size ?: -1)
                ApiResult.Success(response.body() ?: listOf())
            } else{
                ApiResult.Error(response.message())
            }
        } catch (e: Exception){
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

            if(response.isSuccessful){
                ApiResult.Success(response.body() ?: Player(id = null, login = "", ready = false))
            } else{
                ApiResult.Error(response.message())
            }
        } catch (e: Exception){
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
        if(sessionStorage.currentPlayer != null) {
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

}
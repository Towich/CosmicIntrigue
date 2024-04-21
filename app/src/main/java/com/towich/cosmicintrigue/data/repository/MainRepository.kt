package com.towich.cosmicintrigue.data.repository

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
        onReceivedPlayersList: (players: List<Player>) -> Unit
    ): Disposable

    suspend fun getStartTaskMarks(): ApiResult<List<TaskGeoPositionModel>>
}
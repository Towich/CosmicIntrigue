package com.towich.cosmicintrigue.data.repository

import com.towich.cosmicintrigue.data.model.GeoPositionModel
import com.towich.cosmicintrigue.data.model.TaskGeoPositionModel
import com.towich.cosmicintrigue.data.network.ApiResult
import io.reactivex.disposables.CompositeDisposable

interface MainRepository {
    fun sendGeoPosition(compositeDisposable: CompositeDisposable, geoPositionModel: GeoPositionModel)
    fun initGeoPositionsStompClient(
        compositeDisposable: CompositeDisposable,
        onReceivedGeoPosition: (geoPosition: GeoPositionModel) -> Unit
    )
    suspend fun getStartTaskMarks(): ApiResult<List<TaskGeoPositionModel>>
}
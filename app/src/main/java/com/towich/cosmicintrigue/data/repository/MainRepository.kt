package com.towich.cosmicintrigue.data.repository

import com.towich.cosmicintrigue.data.model.GeoPositionModel
import io.reactivex.disposables.CompositeDisposable

interface MainRepository {
    fun sendGeoPosition(compositeDisposable: CompositeDisposable, geoPositionModel: GeoPositionModel)
    fun initGeoPositionsStompClient(
        compositeDisposable: CompositeDisposable,
        onReceivedGeoPosition: (geoPosition: GeoPositionModel) -> Unit
    )
}
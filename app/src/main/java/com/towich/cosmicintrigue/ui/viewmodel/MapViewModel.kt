package com.towich.cosmicintrigue.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import androidx.lifecycle.viewModelScope
import com.towich.cosmicintrigue.data.model.GeoPositionModel
import com.towich.cosmicintrigue.data.repository.MainRepository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class MapViewModel(
    private val repository: MainRepository
): ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()


    fun initGeoPositionsStompClient(
        onReceivedGeoPosition: (geoPosition: GeoPositionModel) -> Unit
    ){
        repository.initGeoPositionsStompClient(
            compositeDisposable = compositeDisposable,
            onReceivedGeoPosition = { geoPosition: GeoPositionModel ->
                onReceivedGeoPosition(geoPosition)
            }
        )
    }
    fun sendGeoPosition(geoPosition: GeoPositionModel) {
        repository.sendGeoPosition(compositeDisposable, geoPosition)
    }
}
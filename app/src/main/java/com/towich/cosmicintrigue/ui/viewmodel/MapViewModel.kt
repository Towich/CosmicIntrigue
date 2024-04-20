package com.towich.cosmicintrigue.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import androidx.lifecycle.viewModelScope
import com.towich.cosmicintrigue.data.model.GeoPositionModel
import com.towich.cosmicintrigue.data.model.TaskGeoPositionModel
import com.towich.cosmicintrigue.data.network.ApiResult
import com.towich.cosmicintrigue.data.repository.MainRepository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class MapViewModel(
    private val repository: MainRepository
): ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    val currentTaskMarks: MutableLiveData<List<TaskGeoPositionModel>> by lazy {
        MutableLiveData<List<TaskGeoPositionModel>>()
    }

    fun getStartTaskMarks() {
        viewModelScope.launch {
            when (val result = repository.getStartTaskMarks()) {
                is ApiResult.Success -> {
                    currentTaskMarks.value = result.data
                }

                is ApiResult.Error -> {
                    Log.e("MapViewModel", result.error)
                }
            }
        }

    }

    fun subscribeGeoPosTopic(
        onReceivedGeoPosition: (geoPosition: GeoPositionModel) -> Unit
    ){
        repository.subscribeGeoPosTopic(onReceivedGeoPosition)
    }


    fun sendGeoPosition(geoPosition: GeoPositionModel) {
        repository.sendGeoPosition(compositeDisposable, geoPosition)
    }

    fun subscribeCoordinatesTopic(
        onReceivedCoordinatesList: (listOfTasksGeoPositions: List<TaskGeoPositionModel>) -> Unit
    ){
        repository.subscribeCoordinatesTopic(onReceivedCoordinatesList)
    }


    fun sendTaskGeoPositionModel(taskGeoPositionModel: TaskGeoPositionModel) {
        repository.sendTaskGeoPositionModel(compositeDisposable, taskGeoPositionModel)
    }


}
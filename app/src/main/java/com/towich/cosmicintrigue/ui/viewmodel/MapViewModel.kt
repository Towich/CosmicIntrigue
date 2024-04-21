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
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch

class MapViewModel(
    private val repository: MainRepository
): ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private var disposableGeoPosTopic: Disposable? = null
    private var disposableCoordinatesTopic: Disposable? = null

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
        disposableGeoPosTopic = repository.subscribeGeoPosTopic(onReceivedGeoPosition)
    }


    fun sendGeoPosition(geoPosition: GeoPositionModel) {
        repository.sendGeoPosition(compositeDisposable, geoPosition)
    }

    fun subscribeCoordinatesTopic(
        onReceivedCoordinatesList: (listOfTasksGeoPositions: List<TaskGeoPositionModel>) -> Unit
    ){
        disposableCoordinatesTopic = repository.subscribeCoordinatesTopic(onReceivedCoordinatesList)
    }


    fun sendTaskGeoPositionModel(taskGeoPositionModel: TaskGeoPositionModel) {
        repository.sendTaskGeoPositionModel(compositeDisposable, taskGeoPositionModel)
    }

    fun dispose(){
        if(disposableGeoPosTopic != null)
            compositeDisposable.delete(disposableGeoPosTopic!!)
        if(disposableCoordinatesTopic != null)
            compositeDisposable.delete(disposableCoordinatesTopic!!)
    }

    fun getPlayerId(): Long? {
        return repository.getCurrentPlayer()?.id
    }

    fun setCurrTaskId(id: Long){
        repository.setCurrTaskId(id)
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.dispose()
    }

}
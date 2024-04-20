package com.towich.cosmicintrigue.data.repository

import android.util.Log
import com.google.gson.Gson
import com.towich.cosmicintrigue.data.model.GeoPositionModel
import com.towich.cosmicintrigue.data.model.TaskGeoPositionModel
import com.towich.cosmicintrigue.data.network.ApiResult
import com.towich.cosmicintrigue.data.network.ApiService
import com.towich.cosmicintrigue.data.network.StompController
import com.towich.cosmicintrigue.data.source.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompMessage

class MainRepositoryImpl(
    private val stompController: StompController,
    private val apiService: ApiService,
    private val gson: Gson,
    private val mStompClient: StompClient,
) : MainRepository {
    override fun sendGeoPosition(
        compositeDisposable: CompositeDisposable,
        geoPositionModel: GeoPositionModel
    ) {
        val request = mStompClient.send(Constants.CHAT_LINK_SOCKET, gson.toJson(geoPositionModel))
        compositeDisposable.add(
            request.subscribeOn(Schedulers.io())
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
        val request =
            mStompClient.send(Constants.COORDINATES_LINK_SOCKET, gson.toJson(taskGeoPositionModel))
        compositeDisposable.add(
            request.subscribeOn(Schedulers.io())
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


    override suspend fun getStartTaskMarks(): ApiResult<List<TaskGeoPositionModel>> {
        return try {
            val response: Response<List<TaskGeoPositionModel>> = apiService.getStartTaskMarks()

            if(response.isSuccessful){
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
//                    id = 77, latitude = 55.8010271, longitude = 37.8057306
//                ),
//                TaskGeoPositionModel(
//                    id = 50, latitude = 51.8010271, longitude = 31.8057306
//                ),
//                TaskGeoPositionModel(
//                    id = 55, latitude = 50.8010271 , longitude = 30.8057306
//                )
//            )
//        )
    }
}
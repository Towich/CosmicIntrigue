package com.towich.cosmicintrigue.data.network

import android.content.ClipData.Item
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.towich.cosmicintrigue.data.model.GeoPositionModel
import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.data.model.TaskGeoPositionModel
import com.towich.cosmicintrigue.data.source.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompMessage

class StompController(
    private val mStompClient: StompClient,
    private val gson: Gson,
) {

    fun initGeoPositionsStompClient(
        compositeDisposable: CompositeDisposable,
    ) {

        //подписываемся на состояние WebSocket'a
        val lifecycleSubscribe = mStompClient.lifecycle()
            .subscribeOn(Schedulers.io(), false)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { lifecycleEvent: LifecycleEvent ->
                when (lifecycleEvent.type!!) {
                    LifecycleEvent.Type.OPENED -> Log.d(
                        "StompClient",
                        "Stomp connection opened"
                    )

                    LifecycleEvent.Type.ERROR -> Log.e(
                        "StompClient",
                        "Error",
                        lifecycleEvent.exception
                    )

                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT,
                    LifecycleEvent.Type.CLOSED -> {
                        Log.d("StompClient", "Stomp connection closed")
                    }
                }
            }

        compositeDisposable.add(lifecycleSubscribe)

        // открываем соединение
        if (!mStompClient.isConnected) {
            mStompClient.connect()
        }
    }

    fun subscribeGeoPosTopic(
        onReceivedGeoPosition: (geoPosition: GeoPositionModel) -> Unit
    ): Disposable {
        // Настраиваем подписку на топик
        return mStompClient.topic(Constants.GEO_POS)
            .subscribeOn(Schedulers.io(), false)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage: StompMessage ->
                Log.d("StompClient", "GEO_POS | " + topicMessage.payload)

                // десериализуем сообщение
                val geoPosition: GeoPositionModel =
                    gson.fromJson(topicMessage.payload, GeoPositionModel::class.java)

                Log.i(
                    "StompClient",
                    "GEO_POS | RECEIVED GEOPOSITION: latitude = ${geoPosition.latitude}, longitude = ${geoPosition.longitude}"
                )

                // вызываем коллбэк
                onReceivedGeoPosition(geoPosition)
            },
                {
                    Log.e("StompClient", "GEO_POS | Error!", it) // обработка ошибок
                }
            )
    }

    fun subscribeCoordinatesTopic(
        onReceivedCoordinatesList: (listOfTasksGeoPositions: List<TaskGeoPositionModel>) -> Unit
    ): Disposable {
        // Настраиваем подписку на топик
        return mStompClient.topic(Constants.COORDINATES_TOPIC)
            .subscribeOn(Schedulers.io(), false)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage: StompMessage ->
                Log.d("StompClient", "COORDINATES_TOPIC | " + topicMessage.payload)


                // десериализуем сообщение
                val listOfTaskGeoPositionModels: List<TaskGeoPositionModel> =
//                    Gson().fromJsonList<TaskGeoPositionModel>(topicMessage.payload)
                    gson.fromJson(topicMessage.payload, Array<TaskGeoPositionModel>::class.java).asList()
//                gson.fr

//                val typeToken = object : TypeToken<List<TaskGeoPositionModel>>() {}.type
//                val list = gson.fromJson<List>(topicMessage., )


                Log.i(
                    "StompClient",
                    "COORDINATES_TOPIC | RECEIVED TASKS: size = ${listOfTaskGeoPositionModels.size}"
                )

                for(task in listOfTaskGeoPositionModels){
                    Log.i(
                        "StompClient",
                        "COORDINATES_TOPIC | RECEIVED TASK: longitude = ${task.longitude}, latitude = ${task.latitude}"
                    )
                }


                // вызываем коллбэк
                onReceivedCoordinatesList(listOfTaskGeoPositionModels)
            },
                {
                    Log.e("StompClient", "Error!", it) // обработка ошибок
                }
            )
    }

    fun subscribeUsersTopic(
        onReceivedPlayersList: (players: List<Player>) -> Unit
    ): Disposable {
        // Настраиваем подписку на топик
        return mStompClient.topic(Constants.USER_TOPIC)
            .subscribeOn(Schedulers.io(), false)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage: StompMessage ->
                Log.d("StompClient", "USER_TOPIC | " + topicMessage.payload)


                // десериализуем сообщение
                val playersList: List<Player> =
//                    Gson().fromJsonList<TaskGeoPositionModel>(topicMessage.payload)
                    gson.fromJson(topicMessage.payload, Array<Player>::class.java).asList()
//                gson.fr

//                val typeToken = object : TypeToken<List<TaskGeoPositionModel>>() {}.type
//                val list = gson.fromJson<List>(topicMessage., )


                Log.i(
                    "StompClient",
                    "USER_TOPIC | RECEIVED PLAYERS: size = ${playersList.size}"
                )

                for(player in playersList){
                    Log.i(
                        "StompClient",
                        "USER_TOPIC | RECEIVED TASK: id = ${player.id}, ready = ${player.ready}"
                    )
                }


                // вызываем коллбэк
                onReceivedPlayersList(playersList)
            },
                {
                    Log.e("StompClient", "Error!", it) // обработка ошибок
                }
            )
    }
}

fun <T> Gson.fromJsonList(jsonString: String): List<T> =
    this.fromJson(jsonString, object: TypeToken<ArrayList<T>>() { }.type)
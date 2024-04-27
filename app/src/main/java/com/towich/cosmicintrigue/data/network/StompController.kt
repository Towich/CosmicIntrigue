package com.towich.cosmicintrigue.data.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.towich.cosmicintrigue.data.model.GameState
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
        onOpened: () -> Unit,
        onError: (exception: Exception) -> Unit,
        onFailedServerHeartbeat: () -> Unit,
        onClosed: () -> Unit
    ) {

        //подписываемся на состояние WebSocket'a
        val lifecycleSubscribe = mStompClient.lifecycle()
            .subscribeOn(Schedulers.io(), false)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { lifecycleEvent: LifecycleEvent ->
                when (lifecycleEvent.type!!) {
                    LifecycleEvent.Type.OPENED -> {
                        onOpened()
                        Log.d("StompClient", "Stomp connection opened")
                    }

                    LifecycleEvent.Type.ERROR -> {
                        onError(lifecycleEvent.exception)
                        Log.e("StompClient", "Error", lifecycleEvent.exception)
                    }

                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> {
                        onFailedServerHeartbeat()
                    }

                    LifecycleEvent.Type.CLOSED -> {
                        onClosed()
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

    fun reconnect(){
        if(!mStompClient.isConnected)
            mStompClient.reconnect()
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
                    gson.fromJson(topicMessage.payload, Array<TaskGeoPositionModel>::class.java)
                        .asList()
//                gson.fr

//                val typeToken = object : TypeToken<List<TaskGeoPositionModel>>() {}.type
//                val list = gson.fromJson<List>(topicMessage., )


                Log.i(
                    "StompClient",
                    "COORDINATES_TOPIC | RECEIVED TASKS: size = ${listOfTaskGeoPositionModels.size}"
                )

                for (task in listOfTaskGeoPositionModels) {
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
        onReceivedGameState: (gameState: GameState) -> Unit
    ): Disposable {
        // Настраиваем подписку на топик
        return mStompClient.topic(Constants.USER_TOPIC)
            .subscribeOn(Schedulers.io(), false)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage: StompMessage ->
                Log.d("StompClient", "USER_TOPIC | " + topicMessage.payload)


                // десериализуем сообщение
                val gameState: GameState =
                    gson.fromJson(topicMessage.payload, GameState::class.java)


                Log.i(
                    "StompClient",
                    "USER_TOPIC | RECEIVED GAMESTATE: user list size = ${gameState.users.size}"
                )

                for (player in gameState.users) {
                    Log.i(
                        "StompClient",
                        "USER_TOPIC | RECEIVED GAMESTATE: id = ${player.id}, ready = ${player.ready}"
                    )
                }


                // вызываем коллбэк
                onReceivedGameState(gameState)
            },
                {
                    Log.e("StompClient", "Error!", it) // обработка ошибок
                }
            )
    }


    fun sendPlayerModel(
        compositeDisposable: CompositeDisposable,
        playerModel: Player
    ) {
        compositeDisposable.add(
            mStompClient.send(Constants.USER_LINK_SOCKET, gson.toJson(playerModel))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.d(
                            "StompClient",
                            "SEND PLAYER: id = ${playerModel.id}$, ready = ${playerModel.ready}"
                        )
                    },
                    {
                        Log.e("StompClient", "Stomp error", it)
                    }
                )
        )
    }
}

fun <T> Gson.fromJsonList(jsonString: String): List<T> =
    this.fromJson(jsonString, object : TypeToken<ArrayList<T>>() {}.type)
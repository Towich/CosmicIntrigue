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
    private val gson: Gson
) {

    private var compositeDisposable: CompositeDisposable? = null
    private var geoPosTopicDisposable: Disposable? = null
    private var coordinatesTopicDisposable: Disposable? = null
    private var usersTopicDisposable: Disposable? = null
    private var gameStateTopicDisposable: Disposable? = null

    fun initGeoPositionsStompClient(
        compositeDisposable: CompositeDisposable?,
        onOpened: () -> Unit,
        onError: (exception: Exception) -> Unit,
        onFailedServerHeartbeat: () -> Unit,
        onClosed: () -> Unit
    ) {

        if(compositeDisposable != null) {
            this.compositeDisposable?.dispose()
            this.compositeDisposable = compositeDisposable
        }

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

        this.compositeDisposable?.add(lifecycleSubscribe)

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
    ) {
        if(geoPosTopicDisposable != null){
            geoPosTopicDisposable?.dispose()
            compositeDisposable?.delete(geoPosTopicDisposable!!)
        }

        // Настраиваем подписку на топик
        val disp = mStompClient.topic(Constants.GEO_POS)
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

        geoPosTopicDisposable = disp
        compositeDisposable?.add(disp)

        Log.i("StompController", "compositeDisposable GeoPosTopic | isDisposed = ${compositeDisposable?.isDisposed}")
        Log.i("StompController", "compositeDisposable GeoPosTopic | size = ${compositeDisposable?.size()} disposables")
    }

    fun subscribeCoordinatesTopic(
        onReceivedCoordinatesList: (listOfTasksGeoPositions: List<TaskGeoPositionModel>) -> Unit
    ) {
        if(coordinatesTopicDisposable != null){
            compositeDisposable?.delete(coordinatesTopicDisposable!!)
        }

        // Настраиваем подписку на топик
        val disp = mStompClient.topic(Constants.COORDINATES_TOPIC)
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

        coordinatesTopicDisposable = disp

        compositeDisposable?.add(disp)
    }

    fun subscribeUsersTopic(
        onReceivedPlayers: (players: Array<Player>) -> Unit
    ) {
        if(usersTopicDisposable != null){
            compositeDisposable?.delete(usersTopicDisposable!!)
        }

        // Настраиваем подписку на топик
        val disp = mStompClient.topic(Constants.USER_TOPIC)
            .subscribeOn(Schedulers.io(), false)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage: StompMessage ->
                Log.d("StompClient", "USER_TOPIC | " + topicMessage.payload)


                // десериализуем сообщение
                val players: Array<Player> =
                    gson.fromJson(topicMessage.payload, Array<Player>::class.java)


                Log.i(
                    "StompClient",
                    "USER_TOPIC | RECEIVED USERS: user list size = ${players.size}"
                )

                for (player in players) {
                    Log.i(
                        "StompClient",
                        "USER_TOPIC | RECEIVED USERS: id = ${player.id}, ready = ${player.ready}"
                    )
                }


                // вызываем коллбэк
                onReceivedPlayers(players)
            },
                {
                    Log.e("StompClient", "Error!", it) // обработка ошибок
                }
            )

        usersTopicDisposable = disp
        compositeDisposable?.add(disp)
    }


    fun sendPlayerModel(
        playerModel: Player
    ) {
        compositeDisposable?.add(
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

    fun sendGeoPosition(
        geoPositionModel: GeoPositionModel
    ) {
        compositeDisposable?.add(
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

    fun sendTaskGeoPositionModel(
        taskGeoPositionModel: TaskGeoPositionModel
    ) {
        compositeDisposable?.add(
            mStompClient.send(Constants.COORDINATES_LINK_SOCKET, gson.toJson(taskGeoPositionModel))
                .subscribeOn(Schedulers.io())
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

    // Topic VOTE
    fun subscribeVoteTopic(
        onReceivedPlayerToKick: (playerToKick: Player) -> Unit
    ) {
        // Настраиваем подписку на топик
        val disp = mStompClient.topic(Constants.VOTE_TOPIC)
            .subscribeOn(Schedulers.io(), false)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage: StompMessage ->
                Log.d("StompClient", "VOTE_TOPIC | " + topicMessage.payload)

                // десериализуем сообщение
                val player: Player =
                    gson.fromJson(topicMessage.payload, Player::class.java)

                Log.i(
                    "StompClient",
                    "VOTE_TOPIC | RECEIVED PLAYER TO KICK: name = '${player.login}'")

                // вызываем коллбэк
                onReceivedPlayerToKick(player)
            },
                {
                    Log.e("StompClient", "VOTE_TOPIC | Error!", it) // обработка ошибок
                }
            )

        compositeDisposable?.add(disp)
    }
    fun sendPlayerModelToKick(
        playerModel: Player
    ) {
        compositeDisposable?.add(
            mStompClient.send(Constants.VOTE_LINK_SOCKET, gson.toJson(playerModel))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.d(
                            "StompClient",
                            "SEND PLAYER TO KICK: id = ${playerModel.id}$, login = ${playerModel.login}"
                        )
                    },
                    {
                        Log.e("StompClient", "Stomp error", it)
                    }
                )
        )
    }

    // Topic GameState
    fun subscribeGameStateTopic(
        onReceivedGameState: (gameState: GameState) -> Unit
    ){
        if(gameStateTopicDisposable != null){
            compositeDisposable?.delete(gameStateTopicDisposable!!)
        }

        // Настраиваем подписку на топик
        val disp = mStompClient.topic(Constants.GAME_TOPIC)
            .subscribeOn(Schedulers.io(), false)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage: StompMessage ->
                Log.d("StompClient", "GAME_TOPIC | " + topicMessage.payload)


                // десериализуем сообщение
                val gameState: GameState =
                    gson.fromJson(topicMessage.payload, GameState::class.java)


                Log.i(
                    "StompClient",
                    "GAME_TOPIC | RECEIVED STATE: gameState = ${gameState.gameState}"
                )

                // вызываем коллбэк
                onReceivedGameState(gameState)
            },
                {
                    Log.e("StompClient", "Error!", it) // обработка ошибок
                }
            )

        gameStateTopicDisposable = disp
        compositeDisposable?.add(disp)
    }

    fun sendEmptyToGameStateTopic(){
        compositeDisposable?.add(
            mStompClient.send(Constants.GAME_LINK_SOCKET)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.d(
                            "StompClient",
                            "SEND EMPTY TO GAME STATE: id = 0, gameState = 0"
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
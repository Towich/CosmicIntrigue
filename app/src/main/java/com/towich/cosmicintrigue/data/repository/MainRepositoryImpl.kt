package com.towich.cosmicintrigue.data.repository

import android.util.Log
import com.google.gson.Gson
import com.towich.cosmicintrigue.data.model.GeoPositionModel
import com.towich.cosmicintrigue.data.source.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompMessage

class MainRepositoryImpl(
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

    override fun initGeoPositionsStompClient(
        compositeDisposable: CompositeDisposable,
        onReceivedGeoPosition: (geoPosition: GeoPositionModel) -> Unit
    ) {
        //настраиваем подписку на топик
        val topicSubscribe = mStompClient.topic(Constants.GEO_POS)
            .subscribeOn(Schedulers.io(), false)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ topicMessage: StompMessage ->
                Log.d("StompClient", topicMessage.payload)

                // десериализуем сообщение
                val geoPosition: GeoPositionModel =
                    gson.fromJson(topicMessage.payload, GeoPositionModel::class.java)

                Log.i(
                    "StompClient",
                    "RECEIVED GEOPOSITION: longitude = ${geoPosition.longitude}, latitude = ${geoPosition.latitude}"
                )
//                addMessage(getPosition) // пишем сообщение в БД и в LiveData
                onReceivedGeoPosition(geoPosition)
            },
                {
                    Log.e("StompClient", "Error!", it) //обработка ошибок
                }
            )

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
        compositeDisposable.add(topicSubscribe)

        // открываем соединение
        if (!mStompClient.isConnected) {
            mStompClient.connect()
        }


    }
}
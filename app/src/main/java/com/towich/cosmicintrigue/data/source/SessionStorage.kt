package com.towich.cosmicintrigue.data.source

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.towich.cosmicintrigue.data.model.Player

class SessionStorage(
    context: Context
) {
    var BASE_URL = context
        .getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)
        .getString(Constants.BASE_URL_APP_PREFERENCES, "https://9a21-185-122-29-131.ngrok-free.app") ?: "https://9a21-185-122-29-131.ngrok-free.app"

    init {
        Log.i("SessionStorage", "SessionStorage | BASE_URL has been set to = $BASE_URL")
    }

    var currentPlayer: Player? = null
    var currTaskId: Long? = null

    var currCountTaskCount: MutableLiveData<Int> = MutableLiveData<Int>(0)
    var totalTaskCount: MutableLiveData<Int> = MutableLiveData<Int>(0)
    var currPlayerIdToKill: Long? = null

    var winners: Boolean? = null
}

package com.towich.cosmicintrigue.data.source

import androidx.lifecycle.MutableLiveData
import com.towich.cosmicintrigue.data.model.Player

class SessionStorage {
    var currentPlayer: Player? = null
    var currTaskId: Long? = null

    var currCountTaskCount: MutableLiveData<Int> = MutableLiveData<Int>(0)
    var totalTaskCount: MutableLiveData<Int> = MutableLiveData<Int>(0)
}

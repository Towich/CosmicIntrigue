package com.towich.cosmicintrigue.data.model

import com.towich.cosmicintrigue.ui.adapters.VoteAdapter

data class Player(
    var id: Long? = null,
    var login: String,
    var ready: Boolean = false,
    var isImposter: Boolean? = null
){
    fun convertToAdapterPlayer() = VoteAdapter.AdaptedPlayer(id = id, login = login, isUser = false, isVoted = false, numVotes = 0)
}
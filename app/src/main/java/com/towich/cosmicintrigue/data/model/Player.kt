package com.towich.cosmicintrigue.data.model

data class Player(
    var id: Long? = null,
    var login: String,
    var ready: Boolean = false,
    var imposter: Boolean? = null
)
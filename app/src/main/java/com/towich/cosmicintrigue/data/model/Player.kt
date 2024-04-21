package com.towich.cosmicintrigue.data.model

data class Player(
    var id: Int? = null,
    var login: String,
    var ready: Boolean = false,
    var isImposter: Boolean? = null
)
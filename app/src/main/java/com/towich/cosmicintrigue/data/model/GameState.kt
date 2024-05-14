package com.towich.cosmicintrigue.data.model

data class GameState(
    val users: List<Player>,
    val gameStart: Boolean,
    val gamePause: Boolean
)


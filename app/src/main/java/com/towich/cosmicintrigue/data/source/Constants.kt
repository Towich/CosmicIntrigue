package com.towich.cosmicintrigue.data.source

import com.towich.cosmicintrigue.data.network.ApiRoutes

object Constants {
    // указываем endpoint, на который регистрировали сокет, не забываем добавить /websocket
    const val SOCKET_URL = "${ApiRoutes.BASE_URL}/api/v1/chat/websocket"

    // заводим название топиков
    const val GEO_POS = "/topic/chat"
    const val COORDINATES_TOPIC = "/topic/coordinates"
    const val USER_TOPIC = "/topic/user"
    const val GAME_TOPIC = "/topic/game"
    const val VOTE_TOPIC = "/topic/vote"

    // указываем endpoint метода, на который будем слать сообщения
    const val CHAT_LINK_SOCKET = "/api/v1/chat/sock"
    const val COORDINATES_LINK_SOCKET = "/api/v1/chat/coordinates"
    const val USER_LINK_SOCKET = "/api/v1/chat/user"
    const val GAME_LINK_SOCKET = "/api/v1/chat/game"
    const val VOTE_LINK_SOCKET = "/api/v1/chat/vote"

    // время работы таймера голосования
    const val VOTE_TIMER_MILIS = 20000L
    // время работы таймера задания
    const val TASK_TIMER_MILIS = 5000L

    const val ACTION_DISTANCE = 0.020 // 20 метров
}
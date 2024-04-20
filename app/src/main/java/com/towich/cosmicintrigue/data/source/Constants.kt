package com.towich.cosmicintrigue.data.source

object Constants {
    // указываем endpoint, на который регистрировали сокет, не забываем добавить /websocket
    const val SOCKET_URL = "https://629d-85-159-229-51.ngrok-free.app/api/v1/chat/websocket"
    // заводим название топика
    const val GEO_POS = "/topic/chat"
    // указываем endpoint метода, на который будем слать сообщения
    const val CHAT_LINK_SOCKET = "/api/v1/chat/sock"
}
package com.towich.cosmicintrigue.data.source

object Constants {
    // указываем endpoint, на который регистрировали сокет, не забываем добавить /websocket
    const val SOCKET_URL = "ws://10.0.2.2:8080/api/v1/chat/websocket"
    // заводим название топика
    const val CHAT_TOPIC = "/topic/chat"
    // указываем endpoint метода, на который будем слать сообщения
    const val CHAT_LINK_SOCKET = "/api/v1/chat/sock"
}
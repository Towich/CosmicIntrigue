package com.towich.cosmicintrigue.data.model

data class TaskGeoPositionModel(
    val id: Long,
    val latitude: Double?,
    val longitude: Double?,
    val completed: Boolean? = true
)
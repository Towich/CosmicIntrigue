package com.towich.cosmicintrigue.data.network

import com.towich.cosmicintrigue.data.model.TaskGeoPositionModel
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET(ApiRoutes.START_COORDINATES)
    suspend fun getStartTaskMarks(): Response<List<TaskGeoPositionModel>>
}
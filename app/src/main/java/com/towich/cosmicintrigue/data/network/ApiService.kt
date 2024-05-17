package com.towich.cosmicintrigue.data.network

import com.towich.cosmicintrigue.data.model.Player
import com.towich.cosmicintrigue.data.model.TaskGeoPositionModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET(ApiRoutes.START_COORDINATES)
    suspend fun getStartTaskMarks(): Response<List<TaskGeoPositionModel>>

    @POST(ApiRoutes.USER)
    suspend fun getUserIdByPlayerModel(
        @Body user: Player,
    ): Response<Player>

    @GET(ApiRoutes.RESTART)
    suspend fun restartServer()
}


package ru.youeleven.randomdemo.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.youeleven.randomdemo.data.remote.models.GameInfoResponse
import ru.youeleven.randomdemo.data.remote.models.GameResponse

interface Api {

    @GET("games")
    suspend fun getGames(
        @Query("page") page: Int,
        @Query("search") search: String?,
        @Query("ordering") ordering: String?,
        @Query("pageSize") pageSize: Int = 20
    ): Response<GameResponse>

    @GET("games/{id}")
    suspend fun getGameInfo(@Path("id") id: Int): Response<GameInfoResponse>
}
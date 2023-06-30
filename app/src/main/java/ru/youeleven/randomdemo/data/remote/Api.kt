package ru.youeleven.randomdemo.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.youeleven.randomdemo.data.remote.models.GameInfoResponse
import ru.youeleven.randomdemo.data.remote.models.GameInfoScreenshots
import ru.youeleven.randomdemo.data.remote.models.GameResponse
import ru.youeleven.randomdemo.utils.Consts

interface Api {

    @GET("games")
    suspend fun getGames(
        @Query("page") page: Int,
        @Query("search") search: String?,
        @Query("ordering") ordering: String?,
        @Query("dates") dates: String,
        @Query("page_size") pageSize: Int = Consts.PAGE_SIZE,
        @Query("exclude_additions") excludeAdditions: Boolean = true,
        @Query("metacritic") metacritic: String = "1, 100"
    ): Response<GameResponse>

    @GET("games/{id}")
    suspend fun getGameInfo(@Path("id") id: Int): Response<GameInfoResponse>

    @GET("games/{id}/screenshots")
    suspend fun getGameScreenshots(@Path("id") id: Int, @Query("page_size") pageSize: Int = 10): Response<GameInfoScreenshots>
}
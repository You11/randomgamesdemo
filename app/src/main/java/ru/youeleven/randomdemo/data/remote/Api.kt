package ru.youeleven.randomdemo.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.youeleven.randomdemo.data.remote.models.GameResponse

interface Api {

    @GET("games")
    suspend fun getGames(@Query("page") page: Int, @Query("pageSize") pageSize: Int = 20): Response<GameResponse>

    companion object {
        fun create(): Api {
            return ApiFactory.create("https://api.rawg.io/api/")
        }
    }
}
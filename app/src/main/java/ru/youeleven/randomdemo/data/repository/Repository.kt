package ru.youeleven.randomdemo.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import ru.youeleven.randomdemo.App
import ru.youeleven.randomdemo.data.models.Game
import ru.youeleven.randomdemo.data.remote.Api

class Repository {

    private val api = Api.create()
    private val dao = App.mainDb.dao()


    fun getGames(): Flow<List<Game>> = dao.getGames().map { list -> list.map { gameLocal -> gameLocal.asGame() } }


    suspend fun updateGames() {
        val response = api.getGames()
        if (response.isSuccessful) {
            val data = response.body() ?: return
            val games = data.toGames() ?: return
            dao.insertGame(games.map { it.asGameLocal() })
        }
    }
}
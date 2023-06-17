package ru.youeleven.randomdemo.data.remote.models

import ru.youeleven.randomdemo.data.models.Game

class GameResponse {


    val results: List<Result>? = null

    class Result {

        val id: Int? = null

        val name: String? = null

    }


    fun toGames(): List<Game>? {
        return results?.map {
            if (it.id == null || it.name == null)
                return null
            else
                Game(it.id, it.name)
        }
    }
}
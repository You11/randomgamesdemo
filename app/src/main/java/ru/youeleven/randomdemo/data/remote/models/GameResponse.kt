package ru.youeleven.randomdemo.data.remote.models

import com.squareup.moshi.Json
import ru.youeleven.randomdemo.data.models.Game

class GameResponse {


    val results: List<Result>? = null

    class Result {

        val id: Int? = null

        val name: String? = null

        val rating: Double? = null

        @field:Json(name = "ratings_count")
        val ratingsCount: Int? = null

        @field:Json(name = "background_image")
        val backgroundImage: String? = null
    }


    fun toGames(): List<Game>? {
        return results?.map {
            if (it.id == null || it.name == null)
                return null
            else
                Game(it.id, it.name, it.rating, it.ratingsCount, it.backgroundImage, null, null)
        }
    }
}
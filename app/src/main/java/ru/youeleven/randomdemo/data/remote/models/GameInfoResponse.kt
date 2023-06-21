package ru.youeleven.randomdemo.data.remote.models

import com.squareup.moshi.Json
import ru.youeleven.randomdemo.data.models.Game
import java.util.Date

class GameInfoResponse {

    val id: Int? = null

    val name: String? = null

    val description: String? = null

    val released: Date? = null

    val rating: Double? = null

    @field:Json(name = "ratings_count")
    val ratingsCount: Int? = null

    @field:Json(name = "background_image")
    val backgroundImage: String? = null


    fun toGame(): Game? {
        if (id == null || name == null) return null
        return Game(id, name, rating, ratingsCount, backgroundImage, description, released)
    }
}
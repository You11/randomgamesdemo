package ru.youeleven.randomdemo.data.models

import ru.youeleven.randomdemo.data.local.models.GameFavoriteLocal
import ru.youeleven.randomdemo.data.local.models.GameLocal
import java.util.Date

data class Game(
    val id: Int,
    val name: String,
    val rating: Double?,
    val ratingCount: Int?,
    val backgroundImage: String?,
    val released: Date?,
    val description: String?
) {
    var isFavoriteGame = false

    fun asGameLocal() = GameLocal(id, name, rating, ratingCount, backgroundImage, released)

    fun asGameFavoriteLocal() = GameFavoriteLocal(id, name, rating, ratingCount, backgroundImage, description, released, null)
}
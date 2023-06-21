package ru.youeleven.randomdemo.data.models

import ru.youeleven.randomdemo.data.local.models.GameLocal
import java.util.Date

data class Game(
    val id: Int,
    val name: String,
    val rating: Double?,
    val ratingCount: Int?,
    val backgroundImage: String?,
    val description: String?,
    val released: Date?
) {

    fun asGameLocal() = GameLocal(id, name, rating, ratingCount, backgroundImage, description, released)
}
package ru.youeleven.randomdemo.data.models

import ru.youeleven.randomdemo.data.local.models.GameLocal

data class Game(
    val id: Int,
    val name: String,
    val rating: Double?,
    val ratingCount: Int?,
    val backgroundImage: String?
) {

    fun asGameLocal() = GameLocal(id, name, rating, ratingCount, backgroundImage)
}
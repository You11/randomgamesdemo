package ru.youeleven.randomdemo.data.models

import ru.youeleven.randomdemo.data.local.models.GameLocal

data class Game(
    val id: Int,
    val name: String
) {

    fun asGameLocal() = GameLocal(id, name)
}
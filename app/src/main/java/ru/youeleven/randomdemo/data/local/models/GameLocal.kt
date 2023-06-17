package ru.youeleven.randomdemo.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.youeleven.randomdemo.data.models.Game

@Entity(tableName = "game")
class GameLocal(@PrimaryKey val id: Int,
                val name: String) {


    fun asGame() = Game(id, name)
}
package ru.youeleven.randomdemo.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.youeleven.randomdemo.data.models.Game
import java.util.Date

@Entity(tableName = "game")
class GameLocal(@PrimaryKey val id: Int,
                val name: String,
                val rating: Double?,
                @ColumnInfo(name = "rating_count") val ratingCount: Int?,
                val backgroundImage: String?,
                val released: Date?) {


    fun asGame() = Game(id, name, rating, ratingCount, backgroundImage, released, null)
}
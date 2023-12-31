package ru.youeleven.randomdemo.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.youeleven.randomdemo.data.models.Game
import java.util.Date

@Entity(tableName = "game_favorite")
class GameFavoriteLocal(
    @PrimaryKey val id: Int,
    val name: String,
    val rating: Double?,
    @ColumnInfo(name = "rating_count") val ratingCount: Int?,
    val backgroundImage: String?,
    val description: String?,
    val released: Date?,
    val screenshots: List<String>
) {
    fun asGame() = Game(id, name, rating, ratingCount, backgroundImage, released, description).also {
        it.isFavoriteGame = true
        it.screenshots = screenshots
    }
}
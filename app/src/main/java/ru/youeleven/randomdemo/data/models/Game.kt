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


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Game

        if (id != other.id) return false
        if (name != other.name) return false
        if (rating != other.rating) return false
        if (ratingCount != other.ratingCount) return false
        if (backgroundImage != other.backgroundImage) return false
        if (released != other.released) return false
        if (description != other.description) return false
        if (isFavoriteGame != other.isFavoriteGame) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + (rating?.hashCode() ?: 0)
        result = 31 * result + (ratingCount ?: 0)
        result = 31 * result + (backgroundImage?.hashCode() ?: 0)
        result = 31 * result + (released?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + isFavoriteGame.hashCode()
        return result
    }


}
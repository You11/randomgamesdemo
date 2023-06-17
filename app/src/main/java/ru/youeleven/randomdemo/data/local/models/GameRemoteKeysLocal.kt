package ru.youeleven.randomdemo.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.youeleven.randomdemo.data.models.GameRemoteKeys

@Entity(tableName = "game_remote_keys")
class GameRemoteKeysLocal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gameId: Int,
    val prevKey: Int?,
    val nextKey: Int?
) {
    fun asGameRemoteKeys() = GameRemoteKeys(gameId = gameId, prevKey = prevKey, nextKey = nextKey)
}
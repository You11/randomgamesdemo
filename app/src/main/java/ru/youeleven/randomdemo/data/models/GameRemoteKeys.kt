package ru.youeleven.randomdemo.data.models

import ru.youeleven.randomdemo.data.local.models.GameRemoteKeysLocal

data class GameRemoteKeys(
    val gameId: Int,
    val prevKey: Int?,
    val nextKey: Int?
) {
    fun asGameRemoteKeysLocal() = GameRemoteKeysLocal(gameId = gameId, prevKey = prevKey, nextKey = nextKey)
}
package ru.youeleven.randomdemo.data.local.models

import androidx.room.Embedded
import androidx.room.Relation

data class GameLocalWithRemoteKeys(@Embedded val remoteKeys: GameRemoteKeysLocal,
                                  @Relation(parentColumn = "gameId", entityColumn = "id") val game: GameLocal
)
package ru.youeleven.randomdemo.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.youeleven.randomdemo.data.local.models.GameFavoriteLocal
import ru.youeleven.randomdemo.data.local.models.GameLocal
import ru.youeleven.randomdemo.data.local.models.GameLocalWithRemoteKeys
import ru.youeleven.randomdemo.data.local.models.GameRemoteKeysLocal
import java.util.Date

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGames(game: List<GameLocal>)

    @Query("SELECT * FROM game_remote_keys ORDER BY id")
    fun getGames(): PagingSource<Int, GameLocalWithRemoteKeys>

    @Query("DELETE FROM game")
    fun deleteAllGames()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGamesRemoteKeys(remoteKey: List<GameRemoteKeysLocal>)

    @Query("SELECT * FROM game_remote_keys WHERE id=:id")
    fun getGameRemoteKeys(id: Int): GameRemoteKeysLocal?

    @Query("SELECT * FROM game_remote_keys ORDER BY id DESC LIMIT 1")
    fun getGameLastRemoteKey(): GameRemoteKeysLocal?

    @Query("DELETE FROM game_remote_keys")
    fun clearGameRemoteKeys()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteGame(game: GameFavoriteLocal)

    @Query("SELECT * FROM game_favorite ORDER BY id")
    fun getFavoriteGames(): List<GameFavoriteLocal>

    @Query("SELECT * FROM game_favorite WHERE id=:id")
    fun getFavoriteGame(id: Int): GameFavoriteLocal

    @Query("DELETE FROM game_favorite WHERE id=:id")
    fun deleteFavoriteGame(id: Int)

    @Query("SELECT id FROM game_favorite")
    fun getFavoriteGamesIds(): List<Int>
}
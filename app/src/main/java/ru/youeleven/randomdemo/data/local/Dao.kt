package ru.youeleven.randomdemo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.youeleven.randomdemo.data.local.models.GameLocal

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(game: List<GameLocal>)

    @Query("SELECT * FROM game")
    fun getGames(): Flow<List<GameLocal>>
}
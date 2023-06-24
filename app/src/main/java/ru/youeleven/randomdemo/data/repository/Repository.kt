package ru.youeleven.randomdemo.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import ru.youeleven.randomdemo.data.local.Dao
import ru.youeleven.randomdemo.data.local.models.GameLocalWithRemoteKeys
import ru.youeleven.randomdemo.data.models.Game
import ru.youeleven.randomdemo.data.models.GameWithCount
import ru.youeleven.randomdemo.data.remote.Api
import ru.youeleven.randomdemo.ui.GamesMediator
import ru.youeleven.randomdemo.ui.GamesPagingSource
import ru.youeleven.randomdemo.utils.CallResult
import java.io.IOException
import javax.inject.Inject


class Repository @Inject constructor(
    private val api: Api,
    private val dao: Dao
) {

    suspend fun getGames(page: Int, search: String?, sort: String?): CallResult<GameWithCount> {
        return try {
            val response = api.getGames(page, search, sort)
            if (response.isSuccessful) {
                val data = response.body()

                if (data != null) {
                    val games = data.toGames()
                    if (games != null && data.count != null) {
                        CallResult(GameWithCount(games = games, count = data.count))
                    } else {
                        CallResult(IOException(""))
                    }
                } else {
                    CallResult(IOException("Wrong data"))
                }
            } else {
                CallResult(IOException("Empty body"))
            }
        } catch (e: Exception) {
            CallResult(e)
        }
    }

    suspend fun getGameInfo(id: Int): CallResult<Game> {
        return try {
            val response = api.getGameInfo(id)
            if (response.isSuccessful) {
                val data = response.body()

                if (data != null) {
                    val game = data.toGame()
                    if (game != null) {
                        CallResult(game)
                    } else {
                        CallResult(IOException(""))
                    }
                } else {
                    CallResult(IOException("Wrong data"))
                }
            } else {
                CallResult(IOException("Empty body"))
            }
        } catch (e: Exception) {
            CallResult(e)
        }
    }

    fun insertFavoriteGame(game: Game) {
        dao.insertFavoriteGame(game.asGameFavoriteLocal())
    }

    fun deleteFavoriteGame(id: Int) {
        dao.deleteFavoriteGame(id)
    }

    fun getFavoriteGames(): List<Game> {
        return dao.getFavoriteGames().map { it.asGame() }
    }

    fun getFavoriteGame(id: Int): Game {
        return dao.getFavoriteGame(id).asGame()
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getGamesPaginatedLocal(): Pager<Int, GameLocalWithRemoteKeys> {
        val pagingSourceFactory = { dao.getGames() }

        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = GamesMediator(this),
            pagingSourceFactory = pagingSourceFactory
        )
    }

    fun getGamesPaginatedRemote(search: String?, sort: String?): Pager<Int, Game> {
        val pagingSourceFactory = {
            GamesPagingSource(this, search, sort)
        }

        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = pagingSourceFactory
        )
    }

    fun isFavoriteGame(id: Int): Boolean {
        val ids = dao.getFavoriteGamesIds()
        return ids.contains(id)
    }
}
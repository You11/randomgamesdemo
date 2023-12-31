package ru.youeleven.randomdemo.data.repository

import android.text.format.DateFormat
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.youeleven.randomdemo.data.local.Dao
import ru.youeleven.randomdemo.data.local.models.GameLocalWithRemoteKeys
import ru.youeleven.randomdemo.data.models.Game
import ru.youeleven.randomdemo.data.models.GameWithCount
import ru.youeleven.randomdemo.data.remote.Api
import ru.youeleven.randomdemo.ui.GamesMediator
import ru.youeleven.randomdemo.ui.GamesPagingSource
import ru.youeleven.randomdemo.utils.CallResult
import ru.youeleven.randomdemo.utils.Consts
import java.io.IOException
import java.util.Calendar
import java.util.Date
import javax.inject.Inject


class Repository @Inject constructor(
    private val api: Api,
    private val dao: Dao
) {

    suspend fun getGames(page: Int, search: String?, sort: String?): CallResult<GameWithCount> {
        return try {
            val response = api.getGames(page, search, sort, getDates())
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
            val responseGame = api.getGameInfo(id)
            if (responseGame.isSuccessful) {
                val data = responseGame.body()

                if (data != null) {
                    val game = data.toGame()
                    if (game != null) {
                        val screenshots = getScreenshots(id)
                        if (screenshots.isSuccess) game.screenshots = getScreenshots(id).data
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

    private suspend fun getScreenshots(id: Int): CallResult<List<String>> {
        return try {
            val response = api.getGameScreenshots(id)
            if (response.isSuccessful) {
                val data = response.body()

                if (data != null) {
                    val images = data.results?.map { it.image ?: return CallResult(emptyList()) }
                    CallResult(images ?: emptyList())
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

    fun getGamesPaginated(search: String?, sort: String?): Flow<PagingData<Game>> {
        return if (search == null && sort == null) {
            getGamesPaginatedLocal().flow.map { pagingData -> pagingData.map { it.game.asGame() } }
        } else {
            getGamesPaginatedRemote(search, sort).flow.map { pagingData -> pagingData.map { it } }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun getGamesPaginatedLocal(): Pager<Int, GameLocalWithRemoteKeys> {
        val pagingSourceFactory = { dao.getGames() }

        return Pager(
            config = PagingConfig(pageSize = Consts.PAGE_SIZE),
            remoteMediator = GamesMediator(this),
            pagingSourceFactory = pagingSourceFactory
        )
    }

    private fun getGamesPaginatedRemote(search: String?, sort: String?): Pager<Int, Game> {
        val pagingSourceFactory = {
            GamesPagingSource(this, search, sort)
        }

        return Pager(
            config = PagingConfig(pageSize = Consts.PAGE_SIZE),
            pagingSourceFactory = pagingSourceFactory
        )
    }

    fun isFavoriteGame(id: Int): Boolean {
        val ids = dao.getFavoriteGamesIds()
        return ids.contains(id)
    }

    private fun getDates(): String {
        fun format(date: Date): String {
            return DateFormat.format("yyyy-MM-dd", date).toString()
        }

        val firstDate = format(Calendar.getInstance().also { it.set(1960, 1, 1) }.time)
        val secondDate = format(Date())

        return "$firstDate,$secondDate"
    }
}
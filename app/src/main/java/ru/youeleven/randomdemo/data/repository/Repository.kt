package ru.youeleven.randomdemo.data.repository

import android.graphics.pdf.PdfDocument.Page
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import ru.youeleven.randomdemo.App
import ru.youeleven.randomdemo.data.local.Dao
import ru.youeleven.randomdemo.data.local.models.GameLocalWithRemoteKeys
import ru.youeleven.randomdemo.data.models.Game
import ru.youeleven.randomdemo.data.remote.Api
import ru.youeleven.randomdemo.ui.GamesMediator
import ru.youeleven.randomdemo.utils.CallResult
import java.io.IOException
import javax.inject.Inject


class Repository @Inject constructor(
    private val api: Api,
    private val dao: Dao
) {

    suspend fun getGames(page: Int): CallResult<List<Game>> {
        return try {
            val response = api.getGames(page)
            if (response.isSuccessful) {
                val data = response.body()

                if (data != null) {
                    val games = data.toGames()
                    if (games != null) {
                        CallResult(games)
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

    @OptIn(ExperimentalPagingApi::class)
    fun getGamesPaginated(): Pager<Int, GameLocalWithRemoteKeys> {
        val pagingSourceFactory = { dao.getGames() }

        return Pager(
            config = PagingConfig(pageSize = 20, initialLoadSize = 20),
            remoteMediator = GamesMediator(this),
            pagingSourceFactory = pagingSourceFactory
        )
    }
}
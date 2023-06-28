package ru.youeleven.randomdemo.ui

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import ru.youeleven.randomdemo.App
import ru.youeleven.randomdemo.data.local.AppDatabase
import ru.youeleven.randomdemo.data.models.Game
import ru.youeleven.randomdemo.data.repository.Repository
import ru.youeleven.randomdemo.di.DbEntryPoint
import ru.youeleven.randomdemo.utils.Consts
import java.io.IOException
import kotlin.math.ceil

class GamesPagingSource(
    val repository: Repository,
    val search: String?,
    val sort: String?
) : PagingSource<Int, Game>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {
        return try {
            val pageNumber = params.key ?: 1
            val response = repository.getGames(page = pageNumber, search = search, sort = sort)
            if (response.isSuccess) {
                LoadResult.Page(
                    data = response.data.games,
                    prevKey = null,
                    nextKey = getNextKey(pageNumber, response.data.count)
                )
            } else {
                LoadResult.Error(response.error as Throwable)
            }

        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Game>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private fun getNextKey(currentPage: Int, count: Int): Int? {
        val lastPage = getLastPageNumber(count)
        return if (currentPage < lastPage) currentPage + 1 else null
    }

    private fun getLastPageNumber(count: Int): Int {
        return if (count == 0) return 1 else ceil(count.toDouble() / Consts.PAGE_SIZE).toInt()
    }
}
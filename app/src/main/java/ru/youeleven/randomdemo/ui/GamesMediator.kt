package ru.youeleven.randomdemo.ui

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import retrofit2.HttpException
import ru.youeleven.randomdemo.App
import ru.youeleven.randomdemo.data.local.AppDatabase
import ru.youeleven.randomdemo.data.local.models.GameLocalWithRemoteKeys
import ru.youeleven.randomdemo.data.local.models.GameRemoteKeysLocal
import ru.youeleven.randomdemo.data.repository.Repository
import java.io.IOException

@ExperimentalPagingApi
class GamesMediator(val repository: Repository): RemoteMediator<Int, GameLocalWithRemoteKeys>() {

    private val initialPage = 1
    var db: AppDatabase = getLogDao(App.instance)


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GameLocalWithRemoteKeys>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: initialPage
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val remoteKeys = getLastRemoteKey()
                    val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextKey
                }
            }

            val response = repository.getGames(page = page)
            if (response.isSuccess) {
                db.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        db.dao().clearGameRemoteKeys()
                        db.dao().deleteAllGames()
                    }
                    val prevKey = if (page == initialPage) null else page - 1
                    val nextKey = page + 1
                    val keys = response.data.map { GameRemoteKeysLocal(gameId = it.id, prevKey = prevKey, nextKey = nextKey) }
                    db.dao().insertGamesRemoteKeys(keys)
                    db.dao().insertGames(response.data.map { it.asGameLocal() })
                }

                return MediatorResult.Success(endOfPaginationReached = false)
            } else {
                return MediatorResult.Error(response.error as Throwable)
            }
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getLastRemoteKey(): GameRemoteKeysLocal? {
        return db.withTransaction {
            db.dao().getGameLastRemoteKey()
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, GameLocalWithRemoteKeys>): GameRemoteKeysLocal? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.remoteKeys?.gameId?.let { id ->
                db.withTransaction { db.dao().getGameRemoteKeys(id) }
            }
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface DbProviderEntryPoint {
        fun db(): AppDatabase
    }

    private fun getLogDao(appContext: Context): AppDatabase {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            appContext,
            DbProviderEntryPoint::class.java
        )
        return hiltEntryPoint.db()
    }
}

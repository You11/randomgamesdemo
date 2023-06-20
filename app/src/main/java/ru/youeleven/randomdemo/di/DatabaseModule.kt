package ru.youeleven.randomdemo.di

import android.content.Context
import androidx.room.PrimaryKey
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.youeleven.randomdemo.data.local.AppDatabase
import ru.youeleven.randomdemo.data.local.Dao
import ru.youeleven.randomdemo.data.remote.Api
import ru.youeleven.randomdemo.data.repository.Repository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideDao(database: AppDatabase): Dao {
        return database.dao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }
}
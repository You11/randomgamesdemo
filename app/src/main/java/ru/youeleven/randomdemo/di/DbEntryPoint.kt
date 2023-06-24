package ru.youeleven.randomdemo.di

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import ru.youeleven.randomdemo.data.local.AppDatabase


object DbEntryPoint {

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface DbProviderEntryPoint {
        fun db(): AppDatabase
    }

    fun getLogDao(appContext: Context): AppDatabase {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            appContext,
            DbProviderEntryPoint::class.java
        )
        return hiltEntryPoint.db()
    }
}
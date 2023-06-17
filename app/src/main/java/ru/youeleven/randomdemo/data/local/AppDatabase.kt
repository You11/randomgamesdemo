package ru.youeleven.randomdemo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.youeleven.randomdemo.data.local.models.GameLocal
import ru.youeleven.randomdemo.data.local.models.GameRemoteKeysLocal

@Database(entities = [
    GameLocal::class,
    GameRemoteKeysLocal::class
], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dao(): Dao
}
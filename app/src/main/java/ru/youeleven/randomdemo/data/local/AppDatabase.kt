package ru.youeleven.randomdemo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import ru.youeleven.randomdemo.data.local.models.GameLocal
import ru.youeleven.randomdemo.data.local.models.GameRemoteKeysLocal

@Database(entities = [
    GameLocal::class,
    GameRemoteKeysLocal::class
], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dao(): Dao
}
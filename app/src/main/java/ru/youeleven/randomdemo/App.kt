package ru.youeleven.randomdemo

import android.app.Application
import androidx.room.Room
import ru.youeleven.randomdemo.data.local.AppDatabase

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        mainDb = Room.databaseBuilder(instance, AppDatabase::class.java, "app_database").build()
    }

    companion object {
        lateinit var instance: App
            private set

        lateinit var mainDb: AppDatabase
            private set
    }
}
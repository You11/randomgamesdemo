package ru.youeleven.randomdemo

import android.app.Application
import androidx.room.Room
import dagger.hilt.android.HiltAndroidApp
import ru.youeleven.randomdemo.data.local.AppDatabase

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
            private set
    }
}
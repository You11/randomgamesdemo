package ru.youeleven.randomdemo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.youeleven.randomdemo.data.remote.Api
import ru.youeleven.randomdemo.data.remote.ApiFactory

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {

    @Provides
    fun provideApi(): Api {
        return ApiFactory.create("https://api.rawg.io/api/")
    }
}
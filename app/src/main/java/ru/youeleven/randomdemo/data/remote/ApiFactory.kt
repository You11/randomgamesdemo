package ru.youeleven.randomdemo.data.remote

import com.squareup.moshi.Moshi
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.youeleven.randomdemo.utils.Consts
import java.util.concurrent.TimeUnit


object ApiFactory {

    inline fun <reified T: Any> create(baseUrl: String): T {
        val moshi = Moshi.Builder()
            .add(DateAdapter())
            .build()

        val retrofit = Retrofit.Builder()
            .client(createClient())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(baseUrl)
            .build()

        return retrofit.create(T::class.java)
    }

    fun createClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val request = chain.request()

                val url = request.url.newBuilder()
                    .addQueryParameter("key", Consts.API_KEY)
                    .build()

                val requestBuilder = request.newBuilder().url(url)

                chain.proceed(requestBuilder.build())
            })
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)

        addLoggingInterceptor(builder)

        return builder.build()
    }

    private fun addLoggingInterceptor(httpClientBuilder: OkHttpClient.Builder) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClientBuilder.addInterceptor(loggingInterceptor)
    }
}
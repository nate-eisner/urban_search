package io.eisner.urban_search.di

import io.eisner.urban_search.BuildConfig
import io.eisner.urban_search.data.api.UrbanSearchApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { createNetworkClient("https://mashape-community-urban-dictionary.p.rapidapi.com/") }
    single { get<Retrofit>().create(UrbanSearchApi::class.java) }
}

private class AppendedInterceptor() : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("X-RapidAPI-Host", "mashape-community-urban-dictionary.p.rapidapi.com")
            .addHeader("X-RapidAPI-Key", BuildConfig.API_KEY)
            .build()
        return chain.proceed(newRequest)
    }
}

private val httpClient: OkHttpClient
    get() {
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)

        val clientBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        clientBuilder.addInterceptor(AppendedInterceptor())
        clientBuilder.readTimeout(30, TimeUnit.SECONDS)
        return clientBuilder.build()
    }

private fun createNetworkClient(baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(httpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}
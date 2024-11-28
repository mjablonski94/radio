package com.tunein.model.service.koin

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tunein.model.service.StationsApiService
import com.tunein.model.service.StationsService
import com.tunein.model.service.StationsServiceImpl
import com.tunein.model.service.error.ApiErrorHandler
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val apiModule = module {
    single<StationsService> {
        val moshi = Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val moshiConverterFactory = MoshiConverterFactory.create(moshi)

        val client = OkHttpClient
            .Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(moshiConverterFactory)
            .baseUrl("https://s3-us-west-1.amazonaws.com/")
            .client(client)
            .build()

        val stationsApiService = retrofit.create(StationsApiService::class.java)
        val apiErrorHandler = ApiErrorHandler()

        StationsServiceImpl(stationsApiService, apiErrorHandler, Dispatchers.IO)
    }
}
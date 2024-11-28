package com.tunein.model.service.koin

import com.squareup.moshi.Moshi
import com.tunein.model.service.StationsResponse
import com.tunein.model.service.StationsService
import com.tunein.model.service.StationsServiceImpl
import com.tunein.model.service.error.ApiErrorHandler
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module


val apiModule = module {
    single<StationsService> {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(StationsResponse::class.java)
        val apiErrorHandler = ApiErrorHandler()

        StationsServiceImpl(adapter, apiErrorHandler, Dispatchers.Default)
    }
}
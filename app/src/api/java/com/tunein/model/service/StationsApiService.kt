package com.tunein.model.service

import retrofit2.http.GET

internal interface StationsApiService {

    @GET("cdn-web.tunein.com/stations.json")
    suspend fun getStations(): StationsResponse

}
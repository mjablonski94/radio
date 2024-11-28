package com.tunein.model.repository

interface StationsRepository {

    suspend fun getStations(): List<StationDataModel>
}
package com.tunein.model.service

import com.squareup.moshi.Json

data class StationsResponse(
    @Json(name = "data")
    val data: List<StationApiData>
)


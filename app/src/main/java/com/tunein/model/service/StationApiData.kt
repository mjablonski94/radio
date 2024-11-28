package com.tunein.model.service

import com.squareup.moshi.Json

data class StationApiData(
    @Json(name = "name")
    val name: String,

    @Json(name = "id")
    val id: String,

    @Json(name = "description")
    val description: String?,

    @Json(name = "imgUrl")
    val imageUrl: String?,

    @Json(name = "streamUrl")
    val streamUrl: String,

    @Json(name = "reliability")
    val reliability: Int?,

    @Json(name = "popularity")
    val popularity: Double?,

    @Json(name = "tags")
    val tags: List<String>?,
)

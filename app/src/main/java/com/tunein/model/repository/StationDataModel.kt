package com.tunein.model.repository

data class StationDataModel(
    val id: String,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val streamUrl: String,
    val reliability: Int?,
    val popularity: Double?,
    val tags: List<String>,
)

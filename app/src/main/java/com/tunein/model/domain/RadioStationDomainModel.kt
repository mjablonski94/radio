package com.tunein.model.domain

data class RadioStationDomainModel(
    val id: String,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val streamUrl: String,
    val reliability: Int?,
    val popularity: Double?,
    val tags: List<String>,
)
package com.tunein.model.repository

import com.tunein.TestParameters
import com.tunein.TestParameters.Companion.then
import com.tunein.model.service.StationApiData
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ServiceToRepositoryDataMapperTest(
    private val params: TestParameters<StationApiData, StationDataModel?>
) {
    private val mapper = ServiceToRepositoryDataMapper()

    @Test
    fun map() {
        // given
        val (given, expected) = params

        // when
        val actual = mapper.map(given)

        // then
        assertEquals(expected, actual)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            StationApiData(
                id = "123",
                name = "Jazz FM",
                description = "Relaxing jazz music",
                imageUrl = "http://example.com/image.jpg",
                streamUrl = "http://example.com/stream",
                reliability = 90,
                popularity = 50.5,
                tags = listOf("jazz", "music")
            ) then StationDataModel(
                id = "123",
                name = "Jazz FM",
                description = "Relaxing jazz music",
                imageUrl = "http://example.com/image.jpg",
                streamUrl = "http://example.com/stream",
                reliability = 90,
                popularity = 50.5,
                tags = listOf("jazz", "music")
            ),

            StationApiData(
                id = "",
                name = "Jazz FM",
                description = "Relaxing jazz music",
                imageUrl = "http://example.com/image.jpg",
                streamUrl = "http://example.com/stream",
                reliability = 90,
                popularity = 50.5,
                tags = listOf("jazz", "music")
            ) then null,

            StationApiData(
                id = "123",
                name = "",
                description = "Relaxing jazz music",
                imageUrl = "http://example.com/image.jpg",
                streamUrl = "http://example.com/stream",
                reliability = 90,
                popularity = 50.5,
                tags = listOf("jazz", "music")
            ) then null,

            StationApiData(
                id = "123",
                name = "Jazz FM",
                description = "Relaxing jazz music",
                imageUrl = "http://example.com/image.jpg",
                streamUrl = "",
                reliability = 90,
                popularity = 50.5,
                tags = listOf("jazz", "music")
            ) then null,

            StationApiData(
                id = "123",
                name = "Jazz FM",
                description = "Relaxing jazz music",
                imageUrl = null,
                streamUrl = "http://example.com/stream",
                reliability = 90,
                popularity = 50.5,
                tags = null
            ) then StationDataModel(
                id = "123",
                name = "Jazz FM",
                description = "Relaxing jazz music",
                imageUrl = null,
                streamUrl = "http://example.com/stream",
                reliability = 90,
                popularity = 50.5,
                tags = emptyList()
            )
        )
    }
}

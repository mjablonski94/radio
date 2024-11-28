package com.tunein.model.domain.mappers

import com.tunein.TestParameters
import com.tunein.TestParameters.Companion.then
import com.tunein.model.domain.RadioStationDomainModel
import com.tunein.model.repository.StationDataModel
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class RepositoryToDomainMapperTest(
    private val params: TestParameters<StationDataModel, RadioStationDomainModel>
) {
    private val mapper = RepositoryToDomainMapper()

    @Test
    fun map_stationDataModel_to_radioStationDomainModel() {
        // given
        val (given, expected) = params

        // when
        val actual: RadioStationDomainModel = mapper.map(given)

        // then
        assertEquals(expected, actual)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            StationDataModel(
                id = "123",
                name = "Test Station",
                description = "This is a test radio station.",
                imageUrl = "https://example.com/image.png",
                streamUrl = "https://example.com/stream.mp3",
                reliability = 85,
                popularity = 4.5,
                tags = listOf("pop", "rock")
            ) then RadioStationDomainModel(
                id = "123",
                name = "Test Station",
                description = "This is a test radio station.",
                imageUrl = "https://example.com/image.png",
                streamUrl = "https://example.com/stream.mp3",
                reliability = 85,
                popularity = 4.5,
                tags = listOf("pop", "rock")
            ),
            StationDataModel(
                id = "456",
                name = "Another Station",
                description = "Another description",
                imageUrl = "https://example.com/image2.png",
                streamUrl = "https://example.com/stream2.mp3",
                reliability = 90,
                popularity = 5.0,
                tags = listOf("jazz", "blues")
            ) then RadioStationDomainModel(
                id = "456",
                name = "Another Station",
                description = "Another description",
                imageUrl = "https://example.com/image2.png",
                streamUrl = "https://example.com/stream2.mp3",
                reliability = 90,
                popularity = 5.0,
                tags = listOf("jazz", "blues")
            )
        )
    }
}

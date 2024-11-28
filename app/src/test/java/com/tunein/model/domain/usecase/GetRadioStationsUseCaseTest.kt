package com.tunein.model.domain.usecase

import com.tunein.TestParameters
import com.tunein.TestParameters.Companion.then
import com.tunein.model.domain.RadioStationDomainModel
import com.tunein.model.domain.usecase.GetRadioStationsUseCase.StationsFilter
import com.tunein.model.domain.mappers.RepositoryToDomainMapper
import com.tunein.model.repository.StationDataModel
import com.tunein.model.repository.StationsRepository
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class GetRadioStationsUseCaseTest(
    private val params: TestParameters<StationsFilter, List<RadioStationDomainModel>>
) {
    private val stationsRepository = mockk<StationsRepository>()
    private val repositoryToDomainMapper = RepositoryToDomainMapper()
    private val getRadioStationsUseCase = GetRadioStationsUseCase(stationsRepository, repositoryToDomainMapper)

    @Test
    fun `test GetRadioStationsUseCase action with filters`() = runBlocking {
        // Given
        val (given, expected) = params

        val mockStations = listOf(
            StationDataModel(
                id = "1",
                name = "Station 1",
                description = "Description 1",
                imageUrl = "url1",
                streamUrl = "stream1",
                reliability = 90,
                popularity = 4.0,
                tags = listOf("tag1", "tag2")
            ),
            StationDataModel(
                id = "2",
                name = "Station 2",
                description = "Description 2",
                imageUrl = "url2",
                streamUrl = "stream2",
                reliability = 80,
                popularity = 3.5,
                tags = listOf("tag3")
            )
        )

        coEvery { stationsRepository.getStations() } returns mockStations

        // When
        val actual = getRadioStationsUseCase.action(given)

        // Then
        assertEquals(expected, actual)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            StationsFilter.FetchAll then listOf(
                RadioStationDomainModel(
                    id = "1", name = "Station 1", description = "Description 1", imageUrl = "url1",
                    streamUrl = "stream1", reliability = 90, popularity = 4.0, tags = listOf("tag1", "tag2")
                ),
                RadioStationDomainModel(
                    id = "2", name = "Station 2", description = "Description 2", imageUrl = "url2",
                    streamUrl = "stream2", reliability = 80, popularity = 3.5, tags = listOf("tag3")
                )
            ),

            StationsFilter.FetchByPopularity(4.0) then listOf(
                RadioStationDomainModel(
                    id = "1", name = "Station 1", description = "Description 1", imageUrl = "url1",
                    streamUrl = "stream1", reliability = 90, popularity = 4.0, tags = listOf("tag1", "tag2")
                )
            ),

            StationsFilter.FetchByReliability(85) then listOf(
                RadioStationDomainModel(
                    id = "1", name = "Station 1", description = "Description 1", imageUrl = "url1",
                    streamUrl = "stream1", reliability = 90, popularity = 4.0, tags = listOf("tag1", "tag2")
                )
            ),

            StationsFilter.FetchBySearch("Station 1") then listOf(
                RadioStationDomainModel(
                    id = "1", name = "Station 1", description = "Description 1", imageUrl = "url1",
                    streamUrl = "stream1", reliability = 90, popularity = 4.0, tags = listOf("tag1", "tag2")
                )
            ),

            StationsFilter.FetchByTag("tag1") then listOf(
                RadioStationDomainModel(
                    id = "1", name = "Station 1", description = "Description 1", imageUrl = "url1",
                    streamUrl = "stream1", reliability = 90, popularity = 4.0, tags = listOf("tag1", "tag2")
                )
            )
        )
    }
}

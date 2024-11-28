package com.tunein.model.domain.mappers

import com.tunein.model.domain.RadioStationDomainModel
import com.tunein.ui.radiolist.RadioStationUiModel
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class RadioStationDomainToUiMapperTest(
    private val domainModel: RadioStationDomainModel,
    private val expectedUiModel: RadioStationUiModel
) {

    private val mapper = RadioStationDomainToUiMapper()

    @Test
    fun map_domain_to_ui_model() {
        // when
        val actualUiModel = mapper.map(domainModel)

        // then
        assertEquals(expectedUiModel, actualUiModel)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            arrayOf(
                RadioStationDomainModel(
                    id = "1",
                    name = "Test Station 1",
                    description = "A great station",
                    imageUrl = "https://example.com/image1.jpg",
                    streamUrl = "https://example.com/stream1",
                    reliability = 85,
                    popularity = 4.5,
                    tags = listOf("music", "pop")
                ),
                RadioStationUiModel(
                    id = "1",
                    name = "Test Station 1",
                    description = "A great station",
                    imageUrl = "https://example.com/image1.jpg",
                    streamUrl = "https://example.com/stream1",
                    reliability = 85,
                    popularity = 4.5,
                    tags = listOf("music", "pop")
                )
            ),
            arrayOf(
                RadioStationDomainModel(
                    id = "2",
                    name = "Test Station 2",
                    description = "Another station",
                    imageUrl = "https://example.com/image2.jpg",
                    streamUrl = "https://example.com/stream2",
                    reliability = 90,
                    popularity = 5.0,
                    tags = listOf("news", "talk")
                ),
                RadioStationUiModel(
                    id = "2",
                    name = "Test Station 2",
                    description = "Another station",
                    imageUrl = "https://example.com/image2.jpg",
                    streamUrl = "https://example.com/stream2",
                    reliability = 90,
                    popularity = 5.0,
                    tags = listOf("news", "talk")
                )
            )
        )
    }
}

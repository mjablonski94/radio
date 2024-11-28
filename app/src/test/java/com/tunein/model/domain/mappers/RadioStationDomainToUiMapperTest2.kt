package com.tunein.model.domain.mappers

import com.tunein.model.domain.RadioStationDomainModel
import com.tunein.ui.radiolist.RadioStationUiModel
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class RadioStationUiToDomainMapperTest(
    private val uiModel: RadioStationUiModel,
    private val expectedDomainModel: RadioStationDomainModel
) {

    private val mapper = RadioStationDomainToUiMapper()

    @Test
    fun map_ui_to_domain_model() {
        // when
        val actualDomainModel = mapper.map(uiModel)

        // then
        assertEquals(expectedDomainModel, actualDomainModel)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            arrayOf(
                RadioStationUiModel(
                    id = "1",
                    name = "Test Station 1",
                    description = "A great station",
                    imageUrl = "https://example.com/image1.jpg",
                    streamUrl = "https://example.com/stream1",
                    reliability = 85,
                    popularity = 4.5,
                    tags = listOf("music", "pop")
                ),
                RadioStationDomainModel(
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
                RadioStationUiModel(
                    id = "2",
                    name = "Test Station 2",
                    description = "Another station",
                    imageUrl = "https://example.com/image2.jpg",
                    streamUrl = "https://example.com/stream2",
                    reliability = 90,
                    popularity = 5.0,
                    tags = listOf("news", "talk")
                ),
                RadioStationDomainModel(
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

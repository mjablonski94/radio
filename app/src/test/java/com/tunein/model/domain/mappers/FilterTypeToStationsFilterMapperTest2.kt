package com.tunein.model.domain.mappers

import com.tunein.TestParameters
import com.tunein.TestParameters.Companion.then
import com.tunein.model.domain.usecase.GetRadioStationsUseCase
import com.tunein.model.domain.usecase.GetRadioStationsUseCase.*
import com.tunein.ui.radiolist.FilterType
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class FilterTypeToStationsFilterMapperTest2(
    private val params: TestParameters<StationsFilter, FilterType>
) {
    private val mapper = FilterTypeToStationsFilterMapper()

    @Test
    fun map_stationsFilter_to_filterType() {
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
            StationsFilter.FetchAll then FilterType.None,
            StationsFilter.FetchByPopularity(4.5) then FilterType.Popularity,
            StationsFilter.FetchByReliability(85) then FilterType.Reliability ,
            StationsFilter.FetchBySearch("query") then FilterType.Search,
            StationsFilter.FetchByTag("tagName") then FilterType.Tag
        )
    }
}

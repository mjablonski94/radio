package com.tunein.model.domain.mappers

import com.tunein.TestParameters
import com.tunein.TestParameters.Companion.then
import com.tunein.model.domain.usecase.GetRadioStationsUseCase.StationsFilter
import com.tunein.ui.radiolist.FilterType
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class FilterTypeToStationsFilterMapperTest(
    private val params: TestParameters<Pair<FilterType, String?>, StationsFilter>
) {
    private val mapper = FilterTypeToStationsFilterMapper()

    @Test
    fun map_filterType_to_stationsFilter() {
        // given
        val (given, expected) = params

        // when
        val (filterType, data) = given
        val actual: StationsFilter = mapper.map(filterType, data)

        // then
        assertEquals(expected, actual)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            (FilterType.None to null) then StationsFilter.FetchAll,
            (FilterType.None to "   ") then StationsFilter.FetchAll,
            (FilterType.Popularity to "4.5") then StationsFilter.FetchByPopularity(4.5),
            (FilterType.Popularity to "invalid") then StationsFilter.FetchAll,
            (FilterType.Reliability to "85") then StationsFilter.FetchByReliability(85),
            (FilterType.Reliability to "invalid") then StationsFilter.FetchAll,
            (FilterType.Search to "query") then StationsFilter.FetchBySearch("query"),
            (FilterType.Tag to "tagName") then StationsFilter.FetchByTag("tagName")
        )
    }
}

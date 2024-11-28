package com.tunein.ui.radiolist

import com.tunein.R

sealed class FilterType(val nameResId: Int) {
    data object None : FilterType(R.string.filter_none)
    data object Tag : FilterType(R.string.filter_tag)
    data object Search : FilterType(R.string.filter_search)
    data object Reliability : FilterType(R.string.filter_reliability)
    data object Popularity : FilterType(R.string.filter_popularity)
}
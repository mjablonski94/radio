package com.tunein.ui.radiolist

import android.app.AlertDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tunein.R
import com.tunein.databinding.ActivityRadioBinding
import com.tunein.model.domain.mappers.FilterTypeToStationsFilterMapper
import com.tunein.ui.playback.PlaybackFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class RadioListActivity : FragmentActivity() {

    private val viewModel: RadioListViewModel by viewModel()
    private val mapper: FilterTypeToStationsFilterMapper by inject()

    private var radioListAdapter: RadioListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityRadioBinding.inflate(layoutInflater)
            .also { setContentView(it.root) }
            .onCreate()
    }


    private fun ActivityRadioBinding.onCreate() {
        initRadioListView()
        initRefreshView()
        initFilterView()

        viewModel.state.observe(this@RadioListActivity) {
            onStateChanged(it)
        }
    }

    private fun ActivityRadioBinding.initRefreshView() {
        swipeToRefresh.setOnRefreshListener { viewModel.refreshStations() }
    }

    private fun ActivityRadioBinding.initRadioListView() {
        val radioAdapter = radioListAdapter ?: RadioListAdapter() { onItemClicked(it) }.also {
            radioListAdapter = it
        }

        with(radioList) {
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(this@RadioListActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@RadioListActivity,
                    DividerItemDecoration.VERTICAL
                )
            )

            scheduleLayoutAnimation()
            adapter = radioAdapter
        }
    }

    private fun ActivityRadioBinding.onItemClicked(radioListItem: RadioListItem) {
        viewModel.selectRadioStation(radioListItem.radioStation)
        PlaybackFragment.addFragment(fragmentContainer, supportFragmentManager)
    }

    private fun ActivityRadioBinding.initFilterView() {
        val filtersAdapter = FiltersAdapter(
            //this list should be placed somewhere hidden under abstract provider/interface
            listOf(
                FilterType.None,
                FilterType.Tag,
                FilterType.Search,
                FilterType.Popularity,
                FilterType.Reliability
            ),
            ::onFilterSelected
        )

        with(filterList) {
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(
                this@RadioListActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            addItemDecoration(
                DividerItemDecoration(
                    this@RadioListActivity,
                    DividerItemDecoration.HORIZONTAL
                )
            )

            scheduleLayoutAnimation()
            adapter = filtersAdapter
        }
    }

    private fun onFilterSelected(filterType: FilterType) {
        if (filterType == FilterType.None) {
            viewModel.refreshStations(mapper.map(filterType))
        } else {
            val editText = EditText(this)
            AlertDialog.Builder(this)
                .setTitle(getString(filterType.nameResId))
                .setView(editText)
                .setPositiveButton(R.string.generic_ok) { _, _ ->
                    viewModel.refreshStations(mapper.map(filterType, editText.text.toString()))
                }
                .show()
        }
    }


    private fun ActivityRadioBinding.onStateChanged(state: RadioListState) {
        when (state) {
            is RadioListState.Error -> dispatchError(state.error)
            is RadioListState.Idle -> dispatchIdle(state)
            is RadioListState.Refreshing -> dispatchRefreshing()
        }
    }

    private fun ActivityRadioBinding.dispatchRefreshing() {
        swipeToRefresh.isRefreshing = true
    }

    private fun ActivityRadioBinding.dispatchIdle(state: RadioListState.Idle) {
        swipeToRefresh.isRefreshing = false
        radioListAdapter?.submitList(state.radioStations.map {
            RadioListItem(
                radioStation = it,
                isPlaying = it == state.lastRadioStation?.selectedRadioStation && state.lastRadioStation.hasError.not()
            )
        })
    }

    private fun ActivityRadioBinding.dispatchError(error: String) {
        swipeToRefresh.isRefreshing = false
        Toast.makeText(this@RadioListActivity, error, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        viewModel.releasePlayer()
        super.onDestroy()
    }
}
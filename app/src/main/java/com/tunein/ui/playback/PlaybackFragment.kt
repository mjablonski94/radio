package com.tunein.ui.playback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tunein.components.image.ImageLoader
import com.tunein.databinding.FragmentPlaybackBinding
import com.tunein.ui.radiolist.RadioStationState
import com.tunein.ui.radiolist.RadioListViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class PlaybackFragment : Fragment() {

    private val viewModel: RadioListViewModel by activityViewModel()
    private var binding: FragmentPlaybackBinding? = null

    private val imageLoader: ImageLoader by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentPlaybackBinding
        .inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.nowPlaying.observe(viewLifecycleOwner, ::observeNowPlaying)
        viewModel.playerState.observe(viewLifecycleOwner, ::observePlaybackState)

        binding?.run {
            playPauseButton.setOnClickListener { viewModel.pausePlay() }
            playNextButton.setOnClickListener { viewModel.playNext() }
            playPreviousButton.setOnClickListener { viewModel.playPrevious() }
        }
    }

    private fun observePlaybackState(playerState: PlayerState) {
        binding?.run {
            playPauseButton.text = getString(playerState.buttonTextRes)
            when(playerState) {
                PlayerState.PLAYING, PlayerState.PAUSED -> playPauseButton.isEnabled = true
                PlayerState.ERROR, PlayerState.LOADING -> playPauseButton.isEnabled = false
            }
        }
    }

    private fun observeNowPlaying(radioStationState: RadioStationState?) {
        val nowPlaying = radioStationState ?: return
        binding?.run {
            stationName.text = nowPlaying.selectedRadioStation.name
            nowPlaying.selectedRadioStation.imageUrl?.let { coverUrl -> imageLoader.loadImage(coverImage, coverUrl) }
        }
    }

    companion object {
        private const val TAG = "PlaybackFragment"

        fun addFragment(
            container: ViewGroup,
            fragmentManager: FragmentManager,
        ) {
            val fragment = PlaybackFragment()

            fragmentManager.beginTransaction()
                .add(container.id, fragment)
                .addToBackStack(TAG)
                .commit()
        }
    }
}
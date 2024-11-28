package com.tunein.components.koin

import com.tunein.components.cache.Cache
import com.tunein.components.cache.CacheFirstStrategy
import com.tunein.components.cache.CacheStrategy
import com.tunein.components.cache.RuntimeCache
import com.tunein.components.image.GlideImageLoader
import com.tunein.components.image.ImageLoader
import com.tunein.components.time.TimeProvider
import com.tunein.components.time.TimeProviderImpl
import com.tunein.model.domain.usecase.FindNextStationUseCase
import com.tunein.model.domain.usecase.GetCurrentPlayingMediaIdUseCase
import com.tunein.model.domain.usecase.GetRadioStationsUseCase
import com.tunein.model.domain.usecase.PlaybackControlUseCase
import com.tunein.model.domain.mappers.RepositoryToDomainMapper
import com.tunein.model.repository.ServiceToRepositoryDataMapper
import com.tunein.model.repository.StationDataModel
import com.tunein.model.repository.StationsCachingRepository
import com.tunein.model.repository.StationsRepository
import com.tunein.model.repository.error.DataErrorHandler
import com.tunein.model.domain.mappers.FilterTypeToStationsFilterMapper
import com.tunein.ui.radiolist.RadioListViewModel
import com.tunein.ui.playback.PlaybackService
import com.tunein.model.domain.providers.RadioPlaybackControllerProvider
import com.tunein.model.domain.providers.RadioPlaybackControllerProviderImpl
import com.tunein.model.domain.mappers.RadioStationDomainToUiMapper
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<Cache<List<StationDataModel>>> { RuntimeCache(timeProvider = get()) }
    single<CacheStrategy<List<StationDataModel>>> { CacheFirstStrategy(cache = get()) }
    single<StationsRepository> {
        StationsCachingRepository(
            stationsService = get(),
            cacheStrategy = get(),
            serviceToRepositoryDataMapper = ServiceToRepositoryDataMapper(),
            errorHandler = DataErrorHandler()
        )
    }
    single { RepositoryToDomainMapper() }
    factory {
        GetRadioStationsUseCase(
            stationsRepository = get(),
            repositoryToDomainMapper = get()
        )
    }
    viewModel {
        RadioListViewModel(
            getRadioStationsUseCase = get(),
            getCurrentPlayingMediaIdUseCase = get(),
            playbackControlUseCase = get(),
            findNextStationUseCase = get(),
            radioStationDomainToUiMapper = get(),
            dispatcher = Dispatchers.IO
        )
    }

    single { FilterTypeToStationsFilterMapper() }
    single<ImageLoader> { GlideImageLoader() }
    factory<RadioPlaybackControllerProvider> {
        RadioPlaybackControllerProviderImpl(
            androidContext(),
            PlaybackService::class.java
        )
    }

    factory { GetCurrentPlayingMediaIdUseCase(radioPlaybackControllerProvider = get()) }
    factory { PlaybackControlUseCase(radioPlaybackControllerProvider = get()) }
    factory { FindNextStationUseCase() }

    single<TimeProvider> { TimeProviderImpl() }
    single { RadioStationDomainToUiMapper() }
}

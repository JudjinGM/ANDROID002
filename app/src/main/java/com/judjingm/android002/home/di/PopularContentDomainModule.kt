package com.judjingm.android002.home.di

import com.judjingm.android002.home.domain.useCase.GetPopularMoviesUseCase
import com.judjingm.android002.home.domain.useCase.GetPopularTVShowsUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface PopularContentDomainModule {

    @Binds
    fun bindGetPopularMoviesUseCase(
        impl: GetPopularMoviesUseCase.Base
    ): GetPopularMoviesUseCase

    @Binds
    fun bindGetPopularTvShowsUseCase(
        impl: GetPopularTVShowsUseCase.Base
    ): GetPopularTVShowsUseCase

}
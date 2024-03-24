package com.judjingm.android002.search.di

import com.judjingm.android002.search.domain.useCase.GetSearchMoviesUseCase
import com.judjingm.android002.search.domain.useCase.GetSearchTVShowsUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface SearchContentDomainModule {

    @Binds
    fun bindGetSearchMoviesUseCase(
        impl: GetSearchMoviesUseCase.Base
    ): GetSearchMoviesUseCase

    @Binds
    fun bindGetSearchTvShowsUseCase(
        impl: GetSearchTVShowsUseCase.Base
    ): GetSearchTVShowsUseCase

}
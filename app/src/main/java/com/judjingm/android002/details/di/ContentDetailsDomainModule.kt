package com.judjingm.android002.details.di

import com.judjingm.android002.details.domain.useCase.GetLanguageForAppUseCase
import com.judjingm.android002.details.domain.useCase.GetMovieCreditsUseCase
import com.judjingm.android002.details.domain.useCase.GetMoviesDetailsUseCase
import com.judjingm.android002.details.domain.useCase.GetTvShowCreditsUseCase
import com.judjingm.android002.details.domain.useCase.GetTvShowDetailsUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface ContentDetailsDomainModule {

    @Binds
    fun bindGetMoviesDetailsUseCase(
        impl: GetMoviesDetailsUseCase.Base
    ): GetMoviesDetailsUseCase

    @Binds
    fun bindGetTvShowDetailsUseCase(
        impl: GetTvShowDetailsUseCase.Base
    ): GetTvShowDetailsUseCase

    @Binds
    fun bindGetMovieCreditsUseCase(
        impl: GetMovieCreditsUseCase.Base
    ): GetMovieCreditsUseCase

    @Binds
    fun bindGetTvShowCreditsUseCase(
        impl: GetTvShowCreditsUseCase.Base
    ): GetTvShowCreditsUseCase

    @Binds
    fun bindGetSystemLanguageUseCase(
        impl: GetLanguageForAppUseCase.Base
    ): GetLanguageForAppUseCase

}
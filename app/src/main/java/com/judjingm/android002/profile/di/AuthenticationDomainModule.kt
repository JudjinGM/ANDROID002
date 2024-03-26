package com.judjingm.android002.profile.di

import com.judjingm.android002.profile.domain.useCase.AuthenticationUseCase
import com.judjingm.android002.profile.domain.useCase.ConfirmLoginToProfileUseCase
import com.judjingm.android002.profile.domain.useCase.DeleteInvalidCredentialsUseCase
import com.judjingm.android002.profile.domain.useCase.GetProfileDetailsUseCase
import com.judjingm.android002.profile.domain.useCase.LogoutFromProfileUseCase
import com.judjingm.android002.profile.domain.useCase.RequestLoginToProfileUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface AuthenticationDomainModule {

    @Binds
    fun bindAuthenticationUseCase(
        impl: AuthenticationUseCase.Base
    ): AuthenticationUseCase

    @Binds
    fun bindConfirmLoginToProfileUseCase(
        impl: ConfirmLoginToProfileUseCase.Base
    ): ConfirmLoginToProfileUseCase

    @Binds
    fun bindGetProfileDetailsUseCase(
        impl: GetProfileDetailsUseCase.Base
    ): GetProfileDetailsUseCase

    @Binds
    fun bindLogoutFromProfileUseCase(
        impl: LogoutFromProfileUseCase.Base
    ): LogoutFromProfileUseCase

    @Binds
    fun bindRequestLoginToProfileUseCase(
        impl: RequestLoginToProfileUseCase.Base
    ): RequestLoginToProfileUseCase

    @Binds
    fun bindDeleteInvalidCredentialsUseCase(
        impl: DeleteInvalidCredentialsUseCase.Base
    ): DeleteInvalidCredentialsUseCase

}
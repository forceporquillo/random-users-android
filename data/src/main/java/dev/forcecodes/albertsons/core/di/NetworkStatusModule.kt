package dev.forcecodes.albertsons.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.forcecodes.albertsons.core.api.DefaultNetworkStatusProvider
import dev.forcecodes.albertsons.core.api.NetworkStatusProvider

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkStatusModule {

    @Binds
    abstract fun bindNetworkStateProvider(
        defaultNetworkStatusProvider: DefaultNetworkStatusProvider
    ): NetworkStatusProvider
}

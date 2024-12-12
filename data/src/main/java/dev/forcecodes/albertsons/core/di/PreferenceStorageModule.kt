package dev.forcecodes.albertsons.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.forcecodes.albertsons.core.preference.DataStorePreferenceStorage
import dev.forcecodes.albertsons.domain.source.PreferenceStorage

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferenceStorageModule {

    @Binds
    abstract fun providesPreferenceStorage(
        datastore: DataStorePreferenceStorage
    ): PreferenceStorage

}

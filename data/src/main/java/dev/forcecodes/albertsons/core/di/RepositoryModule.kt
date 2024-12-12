package dev.forcecodes.albertsons.core.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.forcecodes.albertsons.core.UserDetailsRepoImpl
import dev.forcecodes.albertsons.core.UsersRepoImpl
import dev.forcecodes.albertsons.core.preference.dataStore
import dev.forcecodes.albertsons.domain.source.UserDetailsRepository
import dev.forcecodes.albertsons.domain.source.UsersRepo
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindUsersRepo(usersRepoImpl: UsersRepoImpl): UsersRepo

    @Binds
    abstract fun bindUserDetailsRepo(userDetailsRepoImpl: UserDetailsRepoImpl): UserDetailsRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providesDataStorePrefs(
        @ApplicationContext context: Context
    ) = context.dataStore
}

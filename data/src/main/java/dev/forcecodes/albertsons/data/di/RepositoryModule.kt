/**
 * Copyright 2024 strongforce1
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package dev.forcecodes.albertsons.data.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.forcecodes.albertsons.data.UserDetailsRepoImpl
import dev.forcecodes.albertsons.data.UsersRepoImpl
import dev.forcecodes.albertsons.data.preference.dataStore
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
        @ApplicationContext context: Context,
    ) = context.dataStore
}

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
    abstract fun bindNetworkStateProvider(defaultNetworkStatusProvider: DefaultNetworkStatusProvider): NetworkStatusProvider
}

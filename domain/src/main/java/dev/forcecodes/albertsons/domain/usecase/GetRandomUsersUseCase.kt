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
package dev.forcecodes.albertsons.domain.usecase

import dev.forcecodes.albertsons.domain.source.UsersRepo
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import javax.inject.Singleton

// simple use case only
@Singleton
class GetRandomUsersUseCase
    @Inject
    constructor(
        private val userRepo: UsersRepo,
    ) {
        suspend fun loadMoreItems(forceRefresh: Boolean) = userRepo.getUsers(forceRefresh).collect()

        fun getPaginatedUsers(size: Int) = userRepo.getPaginatedData(size)
    }

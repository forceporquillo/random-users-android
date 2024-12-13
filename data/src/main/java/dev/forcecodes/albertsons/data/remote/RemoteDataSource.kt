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
package dev.forcecodes.albertsons.data.remote

import dev.forcecodes.albertsons.data.api.ApiResponse
import dev.forcecodes.albertsons.data.api.RandomUserApiService
import dev.forcecodes.albertsons.data.api.getResponse
import javax.inject.Inject

class RemoteDataSource
    @Inject
    constructor(
        private val randomUserApiService: RandomUserApiService,
    ) {
        suspend fun fetchUsers(fetchSize: Int): ApiResponse<UsersResponse> {
            return getResponse {
                randomUserApiService.fetchRandomUsers(fetchSize)
            }
        }
    }

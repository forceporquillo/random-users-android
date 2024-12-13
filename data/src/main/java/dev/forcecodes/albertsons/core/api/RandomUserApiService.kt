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
package dev.forcecodes.albertsons.core.api

import dev.forcecodes.albertsons.core.remote.UsersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUserApiService {
    @GET("/api")
    suspend fun fetchRandomUsers(
        @Query("results") results: Int,
    ): Response<UsersResponse>
}

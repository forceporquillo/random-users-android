package dev.forcecodes.albertsons.core.remote

import dev.forcecodes.albertsons.core.api.ApiResponse
import dev.forcecodes.albertsons.core.api.RandomUserApiService
import dev.forcecodes.albertsons.core.api.getResponse
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val randomUserApiService: RandomUserApiService
) {

    suspend fun fetchUsers(fetchSize: Int): ApiResponse<UsersResponse> {
        return getResponse {
            randomUserApiService.fetchRandomUsers(fetchSize)
        }
    }
}

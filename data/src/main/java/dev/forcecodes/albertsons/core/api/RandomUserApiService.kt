package dev.forcecodes.albertsons.core.api

import dev.forcecodes.albertsons.core.remote.UsersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUserApiService {

    @GET("/api")
    suspend fun fetchRandomUsers(@Query("results") results: Int): Response<UsersResponse>
}

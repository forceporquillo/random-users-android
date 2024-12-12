package dev.forcecodes.albertsons.domain.source

import dev.forcecodes.albertsons.domain.model.UserDetails
import kotlinx.coroutines.flow.Flow

interface UserDetailsRepository {

    fun getUserDetails(id: String): Flow<Result<UserDetails>>
}

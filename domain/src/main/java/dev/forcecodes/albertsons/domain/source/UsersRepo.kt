package dev.forcecodes.albertsons.domain.source

import androidx.paging.PagingData
import dev.forcecodes.albertsons.domain.model.UserSimpleInfo
import kotlinx.coroutines.flow.Flow

interface UsersRepo {

    suspend fun getUsers(forceRefresh: Boolean): Flow<Result<List<UserSimpleInfo>>>
    suspend fun getUserInfoById(id: String): Flow<Result<UserSimpleInfo>>
    fun getPaginatedData(maxSize: Int): Flow<PagingData<UserSimpleInfo>>
}

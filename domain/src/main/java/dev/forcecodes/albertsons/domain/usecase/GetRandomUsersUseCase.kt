package dev.forcecodes.albertsons.domain.usecase

import dev.forcecodes.albertsons.domain.source.UsersRepo
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import javax.inject.Singleton

// simple use case only
@Singleton
class GetRandomUsersUseCase @Inject constructor(
    private val userRepo: UsersRepo
) {
    suspend fun loadMoreItems(forceRefresh: Boolean) = userRepo.getUsers(forceRefresh).collect()
    fun getPaginatedUsers(size: Int) = userRepo.getPaginatedData(size)
}

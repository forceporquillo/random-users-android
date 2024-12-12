package dev.forcecodes.albertsons.core

import dev.forcecodes.albertsons.core.database.UserDao
import dev.forcecodes.albertsons.core.mapper.UserDetailsMapper
import dev.forcecodes.albertsons.domain.model.UserDetails
import dev.forcecodes.albertsons.domain.source.UserDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDetailsRepoImpl @Inject constructor(
    private val userDetailsMapper: UserDetailsMapper,
    private val userDao: UserDao
): UserDetailsRepository {

    override fun getUserDetails(id: String): Flow<Result<UserDetails>> {
        return userDao.getUserInfoById(id).map {
            Result.success(userDetailsMapper.toDomainModel(it))
        }
    }
}

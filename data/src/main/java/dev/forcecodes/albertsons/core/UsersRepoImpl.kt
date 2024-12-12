package dev.forcecodes.albertsons.core

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import dev.forcecodes.albertsons.core.database.UserDao
import dev.forcecodes.albertsons.core.mapper.UserInfoMapper
import dev.forcecodes.albertsons.core.qualifiers.IoDispatcher
import dev.forcecodes.albertsons.core.remote.RemoteDataSource
import dev.forcecodes.albertsons.domain.model.UserSimpleInfo
import dev.forcecodes.albertsons.domain.source.UsersRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UsersRepoImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val userDao: UserDao,
    private val userInfoDtoMapper: UserInfoMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): UsersRepo {

    // not entirely consumed
    override suspend fun getUsers(forceRefresh: Boolean): Flow<Result<List<UserSimpleInfo>>> {
        // network bounce resource
        return conflateResource(
            cacheSource = { userDao.getUserInfo() },
            remoteSource = { remoteDataSource.fetchUsers(10) },
            accumulator = { response ->
                val dtoEntity = userInfoDtoMapper.toEntityModel(response.results ?: emptyList())
                userDao.saveAllUserInfo(dtoEntity)
            },
            shouldFetch = { cache ->
                forceRefresh || cache.isNullOrEmpty()
            }
        ).map { result ->
            result.map { entity -> entity.map { userInfoDtoMapper.toDomainModel(it) } }
        }.flowOn(ioDispatcher)
    }

    override suspend fun getUserInfoById(id: String): Flow<Result<UserSimpleInfo>> {
        return userDao.getUserInfoById(id).map { entity ->
            Result.success(userInfoDtoMapper.toDomainModel(entity))
        }.flowOn(ioDispatcher)
    }

    override fun getPaginatedData(maxSize: Int): Flow<PagingData<UserSimpleInfo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { userDao.getPagedUsers(maxSize) }
        ).flow
            .map {
                it.map { entity ->
                    userInfoDtoMapper.toDomainModel(entity)
                }
            }
    }
}

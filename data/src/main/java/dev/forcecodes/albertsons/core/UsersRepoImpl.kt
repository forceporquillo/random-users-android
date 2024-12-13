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

class UsersRepoImpl
    @Inject
    constructor(
        private val remoteDataSource: RemoteDataSource,
        private val userDao: UserDao,
        private val userInfoDtoMapper: UserInfoMapper,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    ) : UsersRepo {
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
                },
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
                config =
                    PagingConfig(
                        pageSize = 20,
                        enablePlaceholders = false,
                    ),
                pagingSourceFactory = { userDao.getPagedUsers(maxSize) },
            ).flow
                .map {
                    it.map { entity ->
                        userInfoDtoMapper.toDomainModel(entity)
                    }
                }
        }
    }

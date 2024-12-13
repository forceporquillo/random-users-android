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
package dev.forcecodes.albertsons.data.mapper

import dev.forcecodes.albertsons.data.local.LocationEntity
import dev.forcecodes.albertsons.data.local.LoginEntity
import dev.forcecodes.albertsons.data.local.UserInfoEntity
import dev.forcecodes.albertsons.data.local.toFormattedAddress
import dev.forcecodes.albertsons.data.remote.Location
import dev.forcecodes.albertsons.data.remote.Login
import dev.forcecodes.albertsons.data.remote.UserInfo
import dev.forcecodes.albertsons.domain.model.LoginCredentials
import dev.forcecodes.albertsons.domain.model.UserDetails
import dev.forcecodes.albertsons.domain.model.UserSimpleInfo
import java.util.UUID
import javax.inject.Inject

class UserDetailsMapper
    @Inject
    constructor() {
        fun toDomainModel(userInfoEntity: UserInfoEntity): UserDetails {
            return userInfoEntity.run {
                UserDetails(
                    fullName = fullName,
                    email = email,
                    dob = dobDate,
                    age = dobAge,
                    dateRegistered = registeredDate,
                    phone = phone,
                    cell = cell,
                    address = location?.toFormattedAddress() ?: "",
                    timezone = location?.timezone?.offset ?: "",
                    loginCredentials = login?.toLoginCredentials(),
                )
            }
        }

        private fun LoginEntity.toLoginCredentials() =
            LoginCredentials(
                uuid = this.uuid,
                username = this.username,
                password = this.password,
                salt = this.salt,
                md5 = this.md5,
                sha1 = this.sha1,
                sha256 = this.sha256,
            )
    }

class UserInfoMapper
    @Inject
    constructor() {
        fun toDomainModel(userInfoEntity: UserInfoEntity): UserSimpleInfo {
            return UserSimpleInfo(
                id = userInfoEntity.id,
                fullName = userInfoEntity.fullName ?: "",
                address = userInfoEntity.location?.toFormattedAddress() ?: "",
                email = userInfoEntity.email ?: "",
                thumbnailUrl = userInfoEntity.thumbnailUrl ?: "",
            )
        }

        fun toEntityModel(userInfoList: List<UserInfo>): List<UserInfoEntity> {
            return userInfoList.map { it.toEntity() }
        }
    }

fun UserInfo.toEntity(): UserInfoEntity {
    return UserInfoEntity(
        id = this.login?.uuid ?: UUID.randomUUID().toString(),
        gender = this.gender,
        phone = this.phone,
        cell = this.cell,
        email = this.email,
        thumbnailUrl = this.picture?.large,
        fullName = "${this.name?.first} ${this.name?.last}",
        login = this.login?.toEntity(),
        dobDate = this.dob?.date,
        dobAge = this.dob?.age,
        registeredDate = this.registered?.date,
        registeredAge = this.registered?.age,
        location = this.location?.toEntity(),
    )
}

fun Login.toEntity(): LoginEntity {
    return LoginEntity(
        uuid = this.uuid,
        username = this.username,
        password = this.password,
        salt = this.salt,
        md5 = this.md5,
        sha1 = this.sha1,
        sha256 = this.sha256,
    )
}

fun Location.toEntity(): LocationEntity {
    return LocationEntity(
        country = this.country,
        city = this.city,
        street = this.street,
        timezone = this.timezone,
        postcode = this.postcode,
        coordinates = this.coordinates,
        state = this.state,
    )
}

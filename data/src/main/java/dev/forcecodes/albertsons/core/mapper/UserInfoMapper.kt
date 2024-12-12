package dev.forcecodes.albertsons.core.mapper

import dev.forcecodes.albertsons.core.local.LocationEntity
import dev.forcecodes.albertsons.core.local.LoginEntity
import dev.forcecodes.albertsons.core.local.UserInfoEntity
import dev.forcecodes.albertsons.core.local.toFormattedAddress
import dev.forcecodes.albertsons.core.remote.Location
import dev.forcecodes.albertsons.core.remote.Login
import dev.forcecodes.albertsons.core.remote.UserInfo
import dev.forcecodes.albertsons.domain.model.LoginCredentials
import dev.forcecodes.albertsons.domain.model.UserDetails
import dev.forcecodes.albertsons.domain.model.UserSimpleInfo
import java.util.UUID
import javax.inject.Inject

class UserDetailsMapper @Inject constructor() {

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
                loginCredentials = login?.toLoginCredentials()
            )
        }
    }

    private fun LoginEntity.toLoginCredentials() = LoginCredentials(
        uuid = this.uuid,
        username = this.username,
        password = this.password,
        salt = this.salt,
        md5 = this.md5,
        sha1 = this.sha1,
        sha256 = this.sha256
    )

}

class UserInfoMapper @Inject constructor() {

    fun toDomainModel(userInfoEntity: UserInfoEntity): UserSimpleInfo {
        return UserSimpleInfo(
            id = userInfoEntity.id,
            fullName = userInfoEntity.fullName ?: "",
            address = userInfoEntity.location?.toFormattedAddress() ?: "",
            email = userInfoEntity.email ?: "",
            thumbnailUrl = userInfoEntity.thumbnailUrl ?: ""
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
        location = this.location?.toEntity()
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
        sha256 = this.sha256
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
        state = this.state
    )
}

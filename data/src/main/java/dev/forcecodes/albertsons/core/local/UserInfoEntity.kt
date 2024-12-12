package dev.forcecodes.albertsons.core.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.forcecodes.albertsons.core.remote.Coordinates
import dev.forcecodes.albertsons.core.remote.Street
import dev.forcecodes.albertsons.core.remote.Timezone

@Entity(
//    tableName = "user_info",
//    foreignKeys = [
//        ForeignKey(
//            entity = LoginEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["loginUuid"],
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
)
data class UserInfoEntity(

    @PrimaryKey
    val id: String,

    // foreign key
//    val loginUuid: String?,

    val gender: String?,
    val phone: String?,
    val cell: String?,
    val email: String?,
    val thumbnailUrl: String?,
    val fullName: String?,

    @Embedded
    val login: LoginEntity?,

    val dobDate: String?,
    val dobAge: Int?,

    val registeredDate: String?,
    val registeredAge: Int?,

    @Embedded
    val location: LocationEntity?,
)

//@Entity
data class LoginEntity(
    val uuid: String?,
    val username: String?,
    val password: String?,
    val salt: String?,
    val md5: String?,
    val sha1: String?,
    val sha256: String?
)

data class NameEntity(
    val last: String? = null,
    val title: String? = null,
    val first: String? = null
)

data class DobEntity(
    val date: String? = null,
    val age: Int? = null
)

data class RegisteredEntity(
    val date: String? = null,
    val age: Int? = null
)

data class LocationEntity(
    val country: String? = null,
    val city: String? = null,

    @Embedded
    val street: Street? = null,

    @Embedded
    val timezone: Timezone? = null,
    val postcode: String? = null,

    @Embedded
    val coordinates: Coordinates? = null,
    val state: String? = null
)

internal fun NameEntity.fullName(): String {
    return "$first $last"
}

internal fun LocationEntity.toFormattedAddress(): String {
    val streetLine = street?.let { "${it.number} ${it.name}" } ?: "Street information not available"
    val cityLine = city ?: "City not available"
    val stateLine = state ?: "State not available"
    val postcodeLine = postcode ?: "Postcode not available"
    val countryLine = country ?: "Country not available"

    return "$streetLine, $cityLine, $stateLine, $postcodeLine, $countryLine"
}

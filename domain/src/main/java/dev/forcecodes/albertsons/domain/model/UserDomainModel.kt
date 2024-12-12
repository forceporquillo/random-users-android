package dev.forcecodes.albertsons.domain.model

data class UserSimpleInfo(
    val id: String,
    val fullName: String,
    val address: String,
    val email: String,
    val thumbnailUrl: String
)

data class UserDetails(
    val fullName: String? = null,
    val email: String? = null,
    val dob: String? = null,
    val age: Int? = null,
    val dateRegistered: String? = null,
    val phone: String? = null,
    val cell: String? = null,
    val address: String? = null,
    val timezone: String? = null,
    val loginCredentials: LoginCredentials? = null
)

data class LoginCredentials(
    val uuid: String? = null,
    val username: String? = null,
    val password: String? = null,
    val salt: String? = null,
    val md5: String? = null,
    val sha1: String? = null,
    val sha256: String? = null
)

package dev.forcecodes.albertsons.core.remote

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class UsersResponse(

	@Json(name="results")
	val results: List<UserInfo>? = null,

	@Json(name="info")
	val info: Info? = null
)

@JsonClass(generateAdapter = true)
data class Street(

	@Json(name="number")
	val number: Int? = null,

	@Json(name="name")
	val name: String? = null
)

@JsonClass(generateAdapter = true)
data class Info(

	@Json(name="seed")
	val seed: String? = null,

	@Json(name="page")
	val page: Int? = null,

	@Json(name="results")
	val results: Int? = null,

	@Json(name="version")
	val version: String? = null
)

@JsonClass(generateAdapter = true)
data class Timezone(

	@Json(name="offset")
	val offset: String? = null,

	@Json(name="description")
	val description: String? = null
)

@JsonClass(generateAdapter = true)
data class UserInfo(

	@Json(name="nat")
	val nat: String? = null,

	@Json(name="gender")
	val gender: String? = null,

	@Json(name="phone")
	val phone: String? = null,

	@Json(name="dob")
	val dob: Dob? = null,

	@Json(name="name")
	val name: Name? = null,

	@Json(name="registered")
	val registered: Registered? = null,

	@Json(name="location")
	val location: Location? = null,

	@Json(name="id")
	val id: Id? = null,

	@Json(name="cell")
	val cell: String? = null,

	@Json(name="email")
	val email: String? = null,

	@Json(name="login")
	val login: Login? = null,

	@Json(name="picture")
	val picture: Picture? = null
)

@JsonClass(generateAdapter = true)
data class Login(
	@Json(name = "uuid")
	val uuid: String? = null,

	@Json(name = "username")
	val username: String? = null,

	@Json(name = "password")
	val password: String? = null,

	@Json(name = "salt")
	val salt: String? = null,

	@Json(name = "md5")
	val md5: String? = null,

	@Json(name = "sha1")
	val sha1: String? = null,

	@Json(name = "sha256")
	val sha256: String? = null
)

@JsonClass(generateAdapter = true)
data class Name(

	@Json(name="last")
	val last: String? = null,

	@Json(name="title")
	val title: String? = null,

	@Json(name="first")
	val first: String? = null
)

@JsonClass(generateAdapter = true)
data class Dob(

	@Json(name="date")
	val date: String? = null,

	@Json(name="age")
	val age: Int? = null
)

@JsonClass(generateAdapter = true)
data class Registered(

	@Json(name="date")
	val date: String? = null,

	@Json(name="age")
	val age: Int? = null
)

@JsonClass(generateAdapter = true)
data class Picture(

	@Json(name="thumbnail")
	val thumbnail: String? = null,

	@Json(name="large")
	val large: String? = null,

	@Json(name="medium")
	val medium: String? = null
)

@JsonClass(generateAdapter = true)
data class Id(

	@Json(name="name")
	val name: String? = null,

	@Json(name="value")
	val value: String? = null
)

@JsonClass(generateAdapter = true)
data class Coordinates(

	@Json(name="latitude")
	val latitude: String? = null,

	@Json(name="longitude")
	val longitude: String? = null
)

@JsonClass(generateAdapter = true)
data class Location(

	@Json(name="country")
	val country: String? = null,

	@Json(name="city")
	val city: String? = null,

	@Json(name="street")
	val street: Street? = null,

	@Json(name="timezone")
	val timezone: Timezone? = null,

	@Json(name="postcode")
	val postcode: String? = null,

	@Json(name="coordinates")
	val coordinates: Coordinates? = null,

	@Json(name="state")
	val state: String? = null
)

package dev.forcecodes.albertsons.core.api

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class ErrorResponse(

	@Json(name="error")
	val error: String? = null
)

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
@file:Suppress("unused")

package dev.forcecodes.albertsons.data.api

/**
 * Wrapper class for handling API responses.
 */
sealed class ApiResponse<T> {
    data class Success<T>(val body: T) : ApiResponse<T>()

    data class Error<T>(val errorMessage: String?, val exception: Exception? = null) : ApiResponse<T>()

    data class Empty<T>(val errorBody: String?) : ApiResponse<T>()
}

suspend inline fun <T> ApiResponse<T>.onSuccess(crossinline block: suspend T.() -> Unit): ApiResponse<T> {
    if (this is ApiResponse.Success<T>) {
        block.invoke(this.body)
    }
    return this
}

inline fun <T> ApiResponse<T>.onError(crossinline block: (String?, Exception?) -> Unit): ApiResponse<T> {
    if (this is ApiResponse.Error<T>) {
        block.invoke(errorMessage, exception)
    }
    return this
}

inline fun <T> ApiResponse<T>.onEmpty(crossinline block: (String?) -> Unit): ApiResponse<T> {
    if (this is ApiResponse.Empty<T>) {
        block.invoke(errorBody)
    }
    return this
}

val ApiResponse<*>.success
    get() = this is ApiResponse.Success && this.body != null

val ApiResponse<*>.error
    get() = this is ApiResponse.Error

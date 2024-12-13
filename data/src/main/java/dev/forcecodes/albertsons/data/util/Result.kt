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
package dev.forcecodes.albertsons.data.util

/**
 * Used to wrap results of an operation that can be successful or unsuccessful.
 * At least one of the values (value or error) must be non-null.
 *
 * @param <S> Result of an operation.
 * @param <F> Type of error that can occur when the operation is executed.
 */
data class Result<out S, out F> private constructor(
    val value: S?,
    val error: F?,
) {
    fun successOrNull(): S? = value

    fun errorOrNull(): F? = error

    /**
     * @suppress
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Result<*, *>

        if (value != other.value) return false
        if (error != other.error) return false

        return true
    }

    /**
     * @suppress
     */
    override fun hashCode(): Int {
        var result = value?.hashCode() ?: 0
        result = 31 * result + (error?.hashCode() ?: 0)
        return result
    }

    companion object {
        @JvmName("success")
        fun <S, F> success(value: S): Result<S, F> = Result(value, null)

        @JvmName("failure")
        fun <S, F> failure(
            fail: F,
            value: S? = null,
        ): Result<S, F> = Result(value, fail)
    }
}

inline fun <R, F, S : R> Result<S, F>.successOrErrorAction(errorAction: (error: F) -> R): R =
    when (val exception = error) {
        null -> value as S
        else -> errorAction(exception)
    }

inline fun <R, S, F> Result<S, F>.fold(
    successAction: (value: S) -> R,
    failureAction: (exception: F) -> R,
    finally: ((Result<S, F>) -> Unit) = {},
): R =
    when (val exception = error) {
        null -> successAction(value as S)
        else -> failureAction(exception)
    }.also { finally(this) }

/**
 * Performs the given [action] if the result of the [Result] represents successful value.
 * The [Result]'s success value is passed to the [action] lambda. Original [Result]
 * is returned for easier chaining.
 */
inline fun <S, F> Result<S, F>.onSuccess(action: (success: S) -> Unit): Result<S, F> {
    if (error == null) value?.let(action)
    return this
}

/**
 * Performs the given [action] if the result of the [Result] represents error value.
 * The [Result]'s error value is passed to the [action] lambda. Original [Result]
 * is returned for easier chaining.
 */
inline fun <S, F> Result<S, F>.onFailure(action: (fail: F) -> Unit): Result<S, F> {
    error?.let(action) // TODO: It would be better to return pair of error and value, so we should refactor this.
    return this
}

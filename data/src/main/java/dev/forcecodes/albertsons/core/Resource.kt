package dev.forcecodes.albertsons.core

import dev.forcecodes.albertsons.core.api.ApiErrorResponse
import dev.forcecodes.albertsons.core.api.ApiResponse
import dev.forcecodes.albertsons.core.api.EmptyResponseException
import dev.forcecodes.albertsons.core.api.onEmpty
import dev.forcecodes.albertsons.core.api.onError
import dev.forcecodes.albertsons.core.api.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

typealias RemoteNetworkCall<Remote> = suspend () -> ApiResponse<Remote>
typealias LocalDatabaseCache<Cache> = suspend () -> Flow<Cache>
typealias ResponseAccumulator<Remote> = suspend (Remote) -> Unit

sealed interface FailureStrategy {
    data object ThrowSilently : FailureStrategy
    data object ThrowOnFailure: FailureStrategy
}

suspend fun <Remote, Response> RemoteNetworkCall<Remote>.execute(
    collector: suspend (Remote) -> Response
) {
    invoke()
        .onSuccess {
            collector.invoke(this)
        }
        .onError { errorMessage, _ ->
            throw ApiErrorResponse(errorMessage)
        }.onEmpty { emptyMessage ->
            throw EmptyResponseException(emptyMessage)
        }
}

internal fun <Local, Remote> conflateResource(
    strategy: FailureStrategy = FailureStrategy.ThrowSilently,
    cacheSource: LocalDatabaseCache<Local>,
    remoteSource: RemoteNetworkCall<Remote>,
    accumulator: ResponseAccumulator<Remote>,
    shouldFetch: (Local?) -> Boolean = { true }
): Flow<Result<Local>> = flow {

    val cache = cacheSource()

    val executeRemoteRequest: suspend () -> Unit = {
        remoteSource.execute(accumulator)
    }

    if (shouldFetch(cache.firstOrNull())) {
        kotlin.runCatching {
            executeRemoteRequest()
        }.onFailure { throwable ->
            if (strategy is FailureStrategy.ThrowOnFailure) {
                throw throwable
            }
        }
    }

    emitAll(cache.map { Result.success(it) })
}
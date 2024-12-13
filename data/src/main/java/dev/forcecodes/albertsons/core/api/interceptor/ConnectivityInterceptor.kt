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
package dev.forcecodes.albertsons.core.api.interceptor

import dev.forcecodes.albertsons.core.api.NetworkConnectionStatus
import dev.forcecodes.albertsons.core.api.NetworkStatusProvider
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException

class ConnectivityInterceptor(
    private val networkStatusProvider: NetworkStatusProvider,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        when (networkStatusProvider.provideNetworkConnectionStatus()) {
            NetworkConnectionStatus.NotConnectedToInternet -> throw NoInternet
            NetworkConnectionStatus.ConnectionTimeOut -> throw ConnectionTimeOut
            else -> return chain.proceed(chain.request())
        }
    }
}

object NoInternet : IOException() {
    private fun readResolve(): Any = NoInternet
}

object ConnectionTimeOut : SocketTimeoutException() {
    private fun readResolve(): Any = ConnectionTimeOut
}

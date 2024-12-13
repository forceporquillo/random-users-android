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
@file:Suppress("MissingPermission")

package dev.forcecodes.albertsons.core.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface NetworkStatusProvider {
    fun provideNetworkConnectionStatus(): NetworkConnectionStatus
}

class DefaultNetworkStatusProvider
    @Inject
    constructor(
        @ApplicationContext context: Context,
    ) :
    NetworkStatusProvider {
        private val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        override fun provideNetworkConnectionStatus(): NetworkConnectionStatus = getNetworkStatus()

        private fun getNetworkStatus(): NetworkConnectionStatus {
            val activeNetwork =
                connectivityManager.activeNetwork
                    ?: return NetworkConnectionStatus.NotConnectedToInternet
            val networkInfo =
                connectivityManager.getNetworkCapabilities(activeNetwork)
                    ?: return NetworkConnectionStatus.NotConnectedToInternet
            with(networkInfo) {
                return when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkConnectionStatus.ConnectedToWifi
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkConnectionStatus.ConnectedToMobileData
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkConnectionStatus.Other
                    else -> NetworkConnectionStatus.NotConnectedToInternet
                }
            }
        }
    }

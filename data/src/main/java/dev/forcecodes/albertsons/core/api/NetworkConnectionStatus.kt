package dev.forcecodes.albertsons.core.api

sealed interface NetworkConnectionStatus {
    data object ConnectedToMobileData: NetworkConnectionStatus
    data object ConnectedToWifi: NetworkConnectionStatus
    data object NotConnectedToInternet: NetworkConnectionStatus
    data object ConnectionTimeOut: NetworkConnectionStatus
    data object Other: NetworkConnectionStatus
}

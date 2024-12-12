package dev.forcecodes.albertsons.domain.source

import kotlinx.coroutines.flow.Flow

interface PreferenceStorage {

    suspend fun selectTheme(theme: String)

    val selectedTheme: Flow<String>
}

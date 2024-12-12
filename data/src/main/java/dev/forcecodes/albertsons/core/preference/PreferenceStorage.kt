package dev.forcecodes.albertsons.core.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.forcecodes.albertsons.core.theme.Theme
import dev.forcecodes.albertsons.domain.source.PreferenceStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

private val PREF_SELECTED_THEME = stringPreferencesKey("pref_dark_mode")

class DataStorePreferenceStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferenceStorage {

    override suspend fun selectTheme(theme: String) {
        dataStore.edit {
            it[PREF_SELECTED_THEME] = theme
        }
    }

    override val selectedTheme =
        dataStore.data.map { it[PREF_SELECTED_THEME] ?: Theme.SYSTEM.storageKey }
}

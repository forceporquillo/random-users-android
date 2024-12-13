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
package dev.forcecodes.albertsons.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.forcecodes.albertsons.core.theme.Theme
import dev.forcecodes.albertsons.domain.source.PreferenceStorage
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

private val PREF_SELECTED_THEME = stringPreferencesKey("pref_dark_mode")

class DataStorePreferenceStorage
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : PreferenceStorage {
        override suspend fun selectTheme(theme: String) {
            dataStore.edit {
                it[PREF_SELECTED_THEME] = theme
            }
        }

        override val selectedTheme =
            dataStore.data.map { it[PREF_SELECTED_THEME] ?: Theme.SYSTEM.storageKey }
    }

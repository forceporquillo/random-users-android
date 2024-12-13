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

package dev.forcecodes.albertsons.randomuser.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.forcecodes.albertsons.core.theme.Theme
import dev.forcecodes.albertsons.domain.usecase.GetAvailableThemesUseCase
import dev.forcecodes.albertsons.domain.usecase.SetThemeUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Thin ViewModel for themed Activities that don't have another ViewModel to use with
 * [ThemedActivityDelegate].
 */

@HiltViewModel
class ThemeViewModel
    @Inject
    constructor(
        themedActivityDelegate: ThemedActivityDelegate,
        private val getAvailableThemesUseCase: GetAvailableThemesUseCase,
        private val setThemeUseCase: SetThemeUseCase,
    ) : ViewModel(), ThemedActivityDelegate by themedActivityDelegate {
        val availableThemes: StateFlow<List<Theme>> =
            flow {
                emit(getAvailableThemesUseCase(GetAvailableThemesUseCase.Params()).getOrNull() ?: listOf())
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf())

        private var themeChangeJob: Job? = null

        fun setTheme(isLight: Boolean) {
            themeChangeJob =
                viewModelScope.launch {
                    val theme = if (isLight) Theme.LIGHT else Theme.DARK
                    setThemeUseCase.invoke(SetThemeUseCase.Params(theme))
                }
        }
    }

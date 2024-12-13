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
package dev.forcecodes.albertsons.randomuser.theme

import dev.forcecodes.albertsons.core.qualifiers.ApplicationScope
import dev.forcecodes.albertsons.core.theme.Theme
import dev.forcecodes.albertsons.domain.usecase.GetThemeUseCase
import dev.forcecodes.albertsons.domain.usecase.ObserveThemeModeUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ThemedActivityDelegateImpl
    @Inject
    constructor(
        @ApplicationScope externalScope: CoroutineScope,
        observeThemeUseCase: ObserveThemeModeUseCase,
        private val getThemeUseCase: GetThemeUseCase,
    ) : ThemedActivityDelegate {
        override val theme: StateFlow<Theme> =
            observeThemeUseCase(ObserveThemeModeUseCase.Params()).map {
                it.getOrDefault(Theme.SYSTEM)
            }.stateIn(externalScope, SharingStarted.Eagerly, Theme.SYSTEM)

        override val currentTheme: Theme
            get() =
                runBlocking { // Using runBlocking to execute this coroutine synchronously
                    getThemeUseCase(GetThemeUseCase.Params()).map {
                        if (it.isSuccess) it.getOrDefault(Theme.SYSTEM) else Theme.SYSTEM
                    }.first()
                }
    }

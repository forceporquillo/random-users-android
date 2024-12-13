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
package dev.forcecodes.albertsons.domain.usecase

import android.os.Build
import dev.forcecodes.albertsons.core.qualifiers.IoDispatcher
import dev.forcecodes.albertsons.core.theme.Theme
import dev.forcecodes.albertsons.core.theme.themeFromStorageKey
import dev.forcecodes.albertsons.domain.FlowUseCase
import dev.forcecodes.albertsons.domain.UseCaseParams
import dev.forcecodes.albertsons.domain.source.PreferenceStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetThemeUseCase
    @Inject
    constructor(
        private val preferenceStorage: PreferenceStorage,
        @IoDispatcher dispatcher: CoroutineDispatcher,
    ) : FlowUseCase<GetThemeUseCase.Params, Theme>(dispatcher) {
        class Params : UseCaseParams.Params()

        override fun execute(parameters: Params): Flow<Result<Theme>> {
            val selectedTheme = preferenceStorage.selectedTheme
            return selectedTheme.map {
                val theme =
                    themeFromStorageKey(it)
                        ?: when {
                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> Theme.SYSTEM
                            else -> Theme.BATTERY_SAVER
                        }

                Result.success(theme)
            }
        }
    }

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

import androidx.core.os.BuildCompat
import dev.forcecodes.albertsons.core.qualifiers.MainImmediateDispatcher
import dev.forcecodes.albertsons.core.theme.Theme
import dev.forcecodes.albertsons.domain.UseCase
import dev.forcecodes.albertsons.domain.UseCaseParams
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAvailableThemesUseCase
    @Inject
    constructor(
        @MainImmediateDispatcher dispatcher: CoroutineDispatcher,
    ) : UseCase<GetAvailableThemesUseCase.Params, List<Theme>>(dispatcher) {
        class Params : UseCaseParams.Params()

        @Suppress("DEPRECATION")
        override suspend fun execute(parameters: Params): List<Theme> =
            when {
                BuildCompat.isAtLeastQ() -> {
                    listOf(Theme.LIGHT, Theme.DARK, Theme.SYSTEM)
                }
                else -> {
                    listOf(Theme.LIGHT, Theme.DARK, Theme.BATTERY_SAVER)
                }
            }
    }

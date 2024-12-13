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

import dev.forcecodes.albertsons.core.qualifiers.IoDispatcher
import dev.forcecodes.albertsons.core.theme.Theme
import dev.forcecodes.albertsons.domain.UseCase
import dev.forcecodes.albertsons.domain.UseCaseParams
import dev.forcecodes.albertsons.domain.source.PreferenceStorage
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class SetThemeUseCase
    @Inject
    constructor(
        private val preferenceStorage: PreferenceStorage,
        @IoDispatcher dispatcher: CoroutineDispatcher,
    ) : UseCase<SetThemeUseCase.Params, Unit>(dispatcher) {
        class Params(val theme: Theme) : UseCaseParams.Params()

        override suspend fun execute(parameters: Params) {
            preferenceStorage.selectTheme(parameters.theme.storageKey)
        }
    }

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
import dev.forcecodes.albertsons.domain.FlowUseCase
import dev.forcecodes.albertsons.domain.UseCaseParams
import dev.forcecodes.albertsons.domain.model.UserDetails
import dev.forcecodes.albertsons.domain.source.UserDetailsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUserDetailsUseCase
    @Inject
    constructor(
        private val userDetailsRepository: UserDetailsRepository,
        @IoDispatcher dispatcher: CoroutineDispatcher,
    ) : FlowUseCase<GetUserDetailsUseCase.Params, UserDetails>(dispatcher) {
        data class Params(val id: String) : UseCaseParams.Params()

        override fun execute(parameters: Params): Flow<Result<UserDetails>> {
            return userDetailsRepository.getUserDetails(parameters.id)
        }
    }

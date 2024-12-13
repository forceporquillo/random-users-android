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
package dev.forcecodes.albertsons.randomuser.presentation.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.forcecodes.albertsons.domain.model.UserSimpleInfo
import dev.forcecodes.albertsons.domain.usecase.GetRandomUsersUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel
    @Inject
    constructor(
        private val getRandUsersUseCase: GetRandomUsersUseCase,
    ) : ViewModel() {
        val fetchSize = MutableStateFlow(DEFAULT_FETCH_SIZE)

        val pagingData =
            fetchSize
                .flatMapLatest {
                    getRandUsersUseCase.getPaginatedUsers(it)
                }.cachedIn(viewModelScope)

        private var nextJob: Job? = null

        fun refresh(forceRefresh: Boolean) {
            // uncomment this line to cancel the previous job
            // fast pagination if commented cause job is not cancelled
            // nextJob?.cancel()
            nextJob =
                viewModelScope.launch {
                    getRandUsersUseCase.loadMoreItems(forceRefresh)
                }
        }

        init {
            fetchSize.value = DEFAULT_FETCH_SIZE
        }
    }

const val DEFAULT_FETCH_SIZE = 10

sealed class ViewState {
    data object Loading : ViewState()

    data class Success(val users: List<UserSimpleInfo>) : ViewState()

    data class Error(val message: String) : ViewState()
}

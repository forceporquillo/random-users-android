package dev.forcecodes.albertsons.randomuser.presentation.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.forcecodes.albertsons.domain.usecase.GetRandomUsersUseCase
import dev.forcecodes.albertsons.domain.model.UserSimpleInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getRandUsersUseCase: GetRandomUsersUseCase
): ViewModel() {

    val fetchSize = MutableStateFlow(DEFAULT_FETCH_SIZE)

    val pagingData = fetchSize
        .flatMapLatest {
        getRandUsersUseCase.getPaginatedUsers(it)
    }.cachedIn(viewModelScope)

    private var nextJob: Job? = null

    fun refresh(forceRefresh: Boolean) {
        // uncomment this line to cancel the previous job
        // fast pagination if commented cause job is not cancelled
        // nextJob?.cancel()
        nextJob = viewModelScope.launch {
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

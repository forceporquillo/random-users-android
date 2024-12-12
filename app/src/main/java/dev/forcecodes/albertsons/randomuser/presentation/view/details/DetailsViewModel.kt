package dev.forcecodes.albertsons.randomuser.presentation.view.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.forcecodes.albertsons.domain.usecase.GetUserDetailsUseCase
import dev.forcecodes.albertsons.domain.model.LoginCredentials
import dev.forcecodes.albertsons.domain.model.UserDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getUserDetailsUseCase: GetUserDetailsUseCase
): ViewModel() {

    private val _userDetailsState = MutableStateFlow<UserDetailsViewState>(UserDetailsViewState.Loading)
    val userDetailsState: StateFlow<UserDetailsViewState> = _userDetailsState

    fun getUserDetails(id: String) {
        viewModelScope.launch {
            getUserDetailsUseCase(GetUserDetailsUseCase.Params(id))
                .debounce(1500L) // simulate a loading call
                .collect {
                    it.onSuccess { userDetails ->
                        val loginCredentials = userDetails.loginCredentials
                        _userDetailsState.value = UserDetailsViewState.Success(
                            userDetails = mapUserDetailsToList(userDetails),
                            loginCredentials = mapLoginCredentialsToList(loginCredentials)
                        )
                    }.onFailure { error ->
                        _userDetailsState.value =
                            UserDetailsViewState.Error(error.message ?: "An unknown error occurred")
                    }
            }
        }
    }

    // hardcoding the keys for now
    private fun mapUserDetailsToList(userDetails: UserDetails): List<Pair<String, String>> {
        return listOf(
            "Dob" to (userDetails.dob ?: "N/A"),
            "Age" to (userDetails.age?.toString() ?: "N/A"),
            "Date Registered" to (userDetails.dateRegistered ?: "N/A"),
            "Phone" to (userDetails.phone ?: "N/A"),
            "Cell" to (userDetails.cell ?: "N/A"),
            "Address" to (userDetails.address ?: "N/A"),
            "Timezone" to (userDetails.timezone ?: "N/A")
        )
    }

    private fun mapLoginCredentialsToList(login: LoginCredentials?): List<Pair<String, String>> {
        return listOf(
            "UUID" to (login?.uuid ?: "N/A"),
            "Username" to (login?.username ?: "N/A"),
            "Password" to (login?.password ?: "N/A"),
            "Salt" to (login?.salt ?: "N/A"),
            "Md5" to (login?.md5 ?: "N/A"),
            "Sha1" to (login?.sha1 ?: "N/A"),
            "Sha256" to (login?.sha256 ?: "N/A")
        )
    }

}

sealed class UserDetailsViewState {

    data object Loading : UserDetailsViewState()

    data class Success(
        val userDetails: List<Pair<String, String>>,
        val loginCredentials: List<Pair<String, String>>
    ) : UserDetailsViewState()

    data class Error(val message: String) : UserDetailsViewState()
}

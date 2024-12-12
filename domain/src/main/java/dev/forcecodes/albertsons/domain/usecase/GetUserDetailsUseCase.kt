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
class GetUserDetailsUseCase @Inject constructor(
    private val userDetailsRepository: UserDetailsRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<GetUserDetailsUseCase.Params, UserDetails>(dispatcher) {

    data class Params(val id: String) : UseCaseParams.Params()

    override fun execute(parameters: Params): Flow<Result<UserDetails>> {
        return userDetailsRepository.getUserDetails(parameters.id)
    }
}

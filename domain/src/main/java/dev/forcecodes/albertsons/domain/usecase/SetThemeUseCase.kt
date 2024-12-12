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
open class SetThemeUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<SetThemeUseCase.Params, Unit>(dispatcher) {

    class Params(val theme: Theme) : UseCaseParams.Params()

    override suspend fun execute(parameters: Params) {
        preferenceStorage.selectTheme(parameters.theme.storageKey)
    }
}

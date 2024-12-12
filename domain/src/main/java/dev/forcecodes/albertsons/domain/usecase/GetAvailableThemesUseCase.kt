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
class GetAvailableThemesUseCase @Inject constructor(
    @MainImmediateDispatcher dispatcher: CoroutineDispatcher
) : UseCase<GetAvailableThemesUseCase.Params, List<Theme>>(dispatcher) {

    class Params : UseCaseParams.Params()

    @Suppress("DEPRECATION")
    override suspend fun execute(parameters: Params): List<Theme> = when {
        BuildCompat.isAtLeastQ() -> {
            listOf(Theme.LIGHT, Theme.DARK, Theme.SYSTEM)
        }
        else -> {
            listOf(Theme.LIGHT, Theme.DARK, Theme.BATTERY_SAVER)
        }
    }
}

package dev.forcecodes.albertsons.domain.usecase

import android.os.Build
import dev.forcecodes.albertsons.core.qualifiers.DefaultDispatcher
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
open class ObserveThemeModeUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    @DefaultDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<ObserveThemeModeUseCase.Params, Theme>(dispatcher) {

    class Params : UseCaseParams.Params()

    override fun execute(parameters: Params): Flow<Result<Theme>> {
        return preferenceStorage.selectedTheme.map {
            val theme = themeFromStorageKey(it)
                ?: when {
                    Build.VERSION.SDK_INT >= 29 -> Theme.SYSTEM
                    else -> Theme.BATTERY_SAVER
                }
            Result.success(theme)
        }
    }
}

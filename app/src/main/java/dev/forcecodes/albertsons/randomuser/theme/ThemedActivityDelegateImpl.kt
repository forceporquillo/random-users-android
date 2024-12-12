package dev.forcecodes.albertsons.randomuser.theme

import dev.forcecodes.albertsons.core.qualifiers.ApplicationScope
import dev.forcecodes.albertsons.core.theme.Theme
import dev.forcecodes.albertsons.domain.usecase.GetThemeUseCase
import dev.forcecodes.albertsons.domain.usecase.ObserveThemeModeUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ThemedActivityDelegateImpl @Inject constructor(
    @ApplicationScope externalScope: CoroutineScope,
    observeThemeUseCase: ObserveThemeModeUseCase,
    private val getThemeUseCase: GetThemeUseCase
) : ThemedActivityDelegate {

    override val theme: StateFlow<Theme> =
        observeThemeUseCase(ObserveThemeModeUseCase.Params()).map {
            it.getOrDefault(Theme.SYSTEM)
        }.stateIn(externalScope, SharingStarted.Eagerly, Theme.SYSTEM)

    override val currentTheme: Theme
        get() = runBlocking { // Using runBlocking to execute this coroutine synchronously
            getThemeUseCase(GetThemeUseCase.Params()).map {
                if (it.isSuccess) it.getOrDefault(Theme.SYSTEM) else Theme.SYSTEM
            }.first()
        }
}

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
package dev.forcecodes.albertsons.randomuser

import dev.forcecodes.albertsons.core.theme.Theme
import dev.forcecodes.albertsons.domain.usecase.GetThemeUseCase
import dev.forcecodes.albertsons.domain.usecase.ObserveThemeModeUseCase
import dev.forcecodes.albertsons.randomuser.theme.ThemedActivityDelegateImpl
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ThemedActivityDelegateImplTest {
    // Dependencies
    private lateinit var observeThemeUseCase: ObserveThemeModeUseCase
    private lateinit var getThemeUseCase: GetThemeUseCase

    // Class under test
    private lateinit var themedActivityDelegate: ThemedActivityDelegateImpl

    @Before
    fun setUp() {
        observeThemeUseCase = mockk()
        getThemeUseCase = mockk()

        themedActivityDelegate =
            ThemedActivityDelegateImpl(
                externalScope = mockk(relaxed = true),
                observeThemeUseCase = observeThemeUseCase,
                getThemeUseCase = getThemeUseCase,
            )
    }

    @Test
    fun `test theme property returns correct theme from ObserveThemeUseCase`() =
        runTest {
            // Mock the ObserveThemeModeUseCase to emit a successful theme
            val observedTheme = Theme.DARK
            coEvery { observeThemeUseCase.invoke(any()) } returns flowOf(Result.success(observedTheme))

            val collectedTheme = mutableListOf<Theme>()
            themedActivityDelegate.theme.take(1).toList(collectedTheme)

            assertEquals(listOf(Theme.DARK), collectedTheme)
        }

    @Test
    fun `test currentTheme returns correct theme from GetThemeUseCase`() =
        runTest {
            // Mock the GetThemeUseCase to emit a successful theme
            val currentTheme = Theme.LIGHT
            coEvery { getThemeUseCase.invoke(any()) } returns flowOf(Result.success(currentTheme))

            val result = themedActivityDelegate.currentTheme
            assertEquals(Theme.LIGHT, result)
        }

    @Test
    fun `test currentTheme falls back to SYSTEM on GetThemeUseCase failure`() =
        runTest {
            // Mock the GetThemeUseCase to emit a failure
            coEvery { getThemeUseCase.invoke(any()) } returns flowOf(Result.failure(Throwable("Error")))

            val result = themedActivityDelegate.currentTheme
            assertEquals(Theme.SYSTEM, result)
        }
}

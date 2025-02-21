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
package dev.forcecodes.albertsons.randomuser.presentation.compose

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment

fun Fragment.requireContentView(
    compositionStrategy: ViewCompositionStrategy = ViewCompositionStrategy.DisposeOnDetachedFromWindow,
    context: Context = requireContext(),
    content: @Composable () -> Unit,
): ComposeView =
    ComposeView(context).apply {
        setViewCompositionStrategy(compositionStrategy)
        setContent {
            // Since we don't have any XML layout to inflate our compose view.
            // Instead, we make use of the Scaffold object to retrieve the
            // inner bottom bar padding.
            Scaffold(
                backgroundColor = Color.Transparent,
            ) { padding ->
                val bottomPadding = padding.calculateBottomPadding()
                Box(
                    modifier = Modifier.padding(PaddingValues(bottom = bottomPadding)),
                ) {
                    content.invoke()
                }
            }
        }
    }

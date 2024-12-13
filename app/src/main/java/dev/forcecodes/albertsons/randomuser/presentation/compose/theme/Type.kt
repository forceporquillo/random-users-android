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
package dev.forcecodes.albertsons.randomuser.presentation.compose.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.forcecodes.albertsons.randomuser.R

val fonts =
    FontFamily(
        Font(
            resId = R.font.proxima_nova_regular,
            weight = FontWeight.Normal,
        ),
        Font(
            resId = R.font.proxima_nova_bold,
            weight = FontWeight.Bold,
        ),
    )

// Set of Material typography styles to start with
val Typography =
    Typography(
        body1 =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            ),
        h6 =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            ),
        caption =
            TextStyle(
                fontFamily = fonts,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
            ),
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
     */
    )

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
package dev.forcecodes.albertsons.randomuser.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.forcecodes.albertsons.core.theme.Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@SuppressLint("WrongConstant")
fun AppCompatActivity.updateForTheme(theme: Theme) =
    when (theme) {
        Theme.DARK -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
        Theme.LIGHT -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
        Theme.SYSTEM -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        Theme.BATTERY_SAVER -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
    }

fun AppCompatActivity.repeatOnLifecycle(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend CoroutineScope.() -> Unit,
) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(lifecycleState, block)
    }
}

fun <T : Any> Activity.newThemedIntent(
    theme: Theme,
    clazz: KClass<T>,
): Intent {
    return Intent(this, clazz.java).apply {
        putExtra("theme", theme)
    }
}

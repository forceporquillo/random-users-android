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

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.forcecodes.albertsons.core.theme.Theme
import dev.forcecodes.albertsons.randomuser.databinding.ActivityMainBinding
import dev.forcecodes.albertsons.randomuser.extensions.repeatOnLifecycle
import dev.forcecodes.albertsons.randomuser.extensions.updateForTheme
import dev.forcecodes.albertsons.randomuser.presentation.view.DashboardViewModel
import dev.forcecodes.albertsons.randomuser.presentation.view.LIMIT_SIZE
import dev.forcecodes.albertsons.randomuser.presentation.view.MAX_USER_LIMIT_KEY
import dev.forcecodes.albertsons.randomuser.presentation.view.SliderDialogFragment
import dev.forcecodes.albertsons.randomuser.theme.ThemeViewModel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    @Suppress("PropertyName")
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupNavigation()
        setupTheme(savedInstanceState)
        setupFragmentResultListener()
        setupFabClickListener()
        observeThemeChanges()
        handleDestinationChanges(binding)
    }

    private fun setupNavigation() {
        navController = findNavController(R.id.nav_host_fragment_content_main)

        val topLevelDestinations = setOf(R.id.xml, R.id.compose)
        appBarConfiguration = AppBarConfiguration(topLevelDestinations)

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)

        supportActionBar?.setHomeButtonEnabled(true)
    }

    private fun setupTheme(savedInstanceState: Bundle?) {
        currentTheme?.let { theme ->
            if (savedInstanceState == null || themeViewModel.currentTheme == theme) {
                updateForTheme(theme)
            }
        }
    }

    private fun setupFragmentResultListener() {
        supportFragmentManager.setFragmentResultListener(MAX_USER_LIMIT_KEY, this) { requestKey, result ->
            if (requestKey == MAX_USER_LIMIT_KEY) {
                dashboardViewModel.fetchSize.value = result.getInt(LIMIT_SIZE)
            }
        }
    }

    private fun setupFabClickListener() {
        binding.fab.setOnClickListener {
            val currentLimitSize = dashboardViewModel.fetchSize.value
            SliderDialogFragment.newInstance(currentLimitSize)
                .show(supportFragmentManager, "slider_dialog")
        }
    }

    private fun observeThemeChanges() {
        repeatOnLifecycle {
            themeViewModel.theme
                .onEach { theme ->
                    binding.toggle.isOn = theme == Theme.LIGHT || !isInDarkMode()
                }
                .debounce(500L)
                .collect {
                    updateForTheme(it)
                }
        }

        binding.toggle.setOnToggledListener { view, isOn ->
            themeViewModel.setTheme(isOn)
            view.isOn = !isOn
        }
    }

    private fun handleDestinationChanges(binding: ActivityMainBinding) {
        val topLevelDestinations = setOf(R.id.xml, R.id.compose)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isTopLevel = topLevelDestinations.contains(destination.id)
            binding.fab.isVisible = isTopLevel
            binding.bottomNav.isVisible = isTopLevel
            binding.toggle.isVisible = isTopLevel
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun isInDarkMode(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    private val currentTheme: Theme?
        get() {
            @Suppress("deprecation")
            return intent.extras?.getSerializable("theme") as? Theme
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

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

    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.bottomNav.setupWithNavController(navController)

        supportActionBar?.setHomeButtonEnabled(true)

        currentTheme?.let {
            when {
                savedInstanceState == null -> {
                    updateForTheme(it)
                }
                themeViewModel.currentTheme == it -> {
                    updateForTheme(it)
                }
            }
        }

        supportFragmentManager.setFragmentResultListener(MAX_USER_LIMIT_KEY, this) { requestKey, result ->
            if (requestKey == MAX_USER_LIMIT_KEY) {
                dashboardViewModel.fetchSize.value = result.getInt(LIMIT_SIZE)
            }
        }

        binding.fab.setOnClickListener {
            val currentLimitSize = dashboardViewModel.fetchSize.value
            SliderDialogFragment.newInstance(currentLimitSize)
                .show(supportFragmentManager, "slider_dialog")
        }

        repeatOnLifecycle {
            themeViewModel.theme
                .onEach {
                    binding.toggle.isOn = it == Theme.LIGHT || !isInDarkMode()
                }
                .debounce(500L)
                .collect {
                    updateForTheme(it)
                }
        }

        binding.toggle.setOnToggledListener { v, isOn ->
            themeViewModel.setTheme(isOn)
            v.isOn = !isOn
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isDetailsScreen = destination.id != R.id.detailsFragment
            binding.fab.isVisible = isDetailsScreen
            binding.bottomNav.isVisible = isDetailsScreen
            binding.toggle.isVisible = isDetailsScreen
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
}

package com.fit5046.elderlycare

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.rememberDrawerState
import com.fit5046.elderlycare.ui.screens.HomePlaceholderScreen
import com.fit5046.elderlycare.ui.screens.MedicationPlaceholderScreen
import com.fit5046.elderlycare.ui.screens.SettingsPlaceholderScreen
import com.fit5046.elderlycare.ui.screens.nutrition.FoodSearchScreen
import com.fit5046.elderlycare.ui.screens.nutrition.NutritionChartScreen
import com.fit5046.elderlycare.ui.screens.nutrition.NutritionLogScreen
import kotlinx.coroutines.launch

private data class DrawerDestination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val matchesRoute: (String?) -> Boolean = { currentRoute -> currentRoute == route }
)

private enum class NutritionTab(val route: String, val label: String) {
    Log("nutrition/log", "Log"),
    Search("nutrition/search", "Search"),
    Chart("nutrition/chart", "Chart")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElderlyCareApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val nutritionRoutes = NutritionTab.entries.map { it.route }
    val drawerDestinations = remember {
        listOf(
            DrawerDestination("home", "Home", Icons.Default.Home),
            DrawerDestination("medication", "Medication", Icons.Default.LocalPharmacy),
            DrawerDestination(
                route = NutritionTab.Log.route,
                label = "Nutrition",
                icon = Icons.Default.RestaurantMenu,
                matchesRoute = { route -> route in nutritionRoutes }
            ),
            DrawerDestination("settings", "Settings", Icons.Default.Settings)
        )
    }
    val currentTitle = when (currentRoute) {
        "home" -> "Home"
        "medication" -> "Medication"
        NutritionTab.Log.route -> "Nutrition Log"
        NutritionTab.Search.route -> "Food Search"
        NutritionTab.Chart.route -> "Nutrition Chart"
        "settings" -> "Settings"
        else -> "Elderly Care App"
    }

    val navigateTo: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 24.dp)) {
                    Text(
                        text = "Navigation",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                    drawerDestinations.forEach { destination ->
                        NavigationDrawerItem(
                            label = { Text(destination.label) },
                            icon = {
                                Icon(
                                    imageVector = destination.icon,
                                    contentDescription = destination.label
                                )
                            },
                            selected = destination.matchesRoute(currentRoute),
                            onClick = {
                                navigateTo(destination.route)
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(currentTitle) },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Open navigation")
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                composable("home") {
                    HomePlaceholderScreen(
                        onOpenNutrition = { navigateTo(NutritionTab.Log.route) },
                        onOpenSearch = { navigateTo(NutritionTab.Search.route) }
                    )
                }
                composable("medication") {
                    MedicationPlaceholderScreen()
                }
                composable(NutritionTab.Log.route) {
                    NutritionSectionLayout(
                        selectedTab = NutritionTab.Log,
                        onTabSelected = { navigateTo(it.route) }
                    ) { contentPadding ->
                        NutritionLogScreen(contentPadding = contentPadding)
                    }
                }
                composable(NutritionTab.Search.route) {
                    NutritionSectionLayout(
                        selectedTab = NutritionTab.Search,
                        onTabSelected = { navigateTo(it.route) }
                    ) { contentPadding ->
                        FoodSearchScreen(contentPadding = contentPadding)
                    }
                }
                composable(NutritionTab.Chart.route) {
                    NutritionSectionLayout(
                        selectedTab = NutritionTab.Chart,
                        onTabSelected = { navigateTo(it.route) }
                    ) { contentPadding ->
                        NutritionChartScreen(contentPadding = contentPadding)
                    }
                }
                composable("settings") {
                    SettingsPlaceholderScreen()
                }
            }
        }
    }
}

@Composable
private fun NutritionSectionLayout(
    selectedTab: NutritionTab,
    onTabSelected: (NutritionTab) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            Text(
                text = "Nutrition Tracking",
                style = MaterialTheme.typography.headlineSmall
            )
        }
        TabRow(selectedTabIndex = NutritionTab.entries.indexOf(selectedTab)) {
            NutritionTab.entries.forEach { tab ->
                Tab(
                    selected = tab == selectedTab,
                    onClick = { onTabSelected(tab) },
                    text = { Text(tab.label) }
                )
            }
        }
        content(PaddingValues(16.dp))
    }
}

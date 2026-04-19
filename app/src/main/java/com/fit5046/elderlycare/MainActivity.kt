package com.fit5046.elderlycare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fit5046.elderlycare.data.FoodItem
import com.fit5046.elderlycare.data.NutritionEntry
import com.fit5046.elderlycare.data.SampleNutritionData
import com.fit5046.elderlycare.data.WeeklyNutrition
import com.fit5046.elderlycare.ui.theme.ElderlyCareTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    HOME("home", "Home", Icons.Default.Home),
    MEDICATION("medication", "Medication", Icons.Default.LocalPharmacy),
    NUTRITION("nutrition", "Nutrition", Icons.Default.RestaurantMenu),
    SETTINGS("settings", "Profile", Icons.Default.Person)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ElderlyCareTheme {
                ElderlyCareApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElderlyCareApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentTitle = when (currentDestination?.route) {
        Destination.HOME.route -> "Home Screen"
        Destination.MEDICATION.route -> "Medication Screen"
        Destination.NUTRITION.route -> "Nutrition Screen"
        Destination.SETTINGS.route -> "My Profile"
        else -> "Elderly Care App"
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationRail(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                NavigationRailItem(
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close drawer"
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Destination.entries.forEach { destination ->
                    NavigationRailItem(
                        selected = currentDestination?.route == destination.route,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            scope.launch { drawerState.close() }
                        },
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = destination.label
                            )
                        },
                        label = {
                            Text(destination.label)
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    ) {
        Scaffold(
            containerColor = Color(0xFFFBF7FF),
            topBar = {
                TopAppBar(
                    title = { Text(currentTitle) },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (drawerState.isClosed) {
                                        drawerState.open()
                                    } else {
                                        drawerState.close()
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFB39DDB),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Destination.HOME.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Destination.HOME.route) {
                    HomeScreen()
                }
                composable(Destination.MEDICATION.route) {
                    MedicationScreen()
                }
                composable(Destination.NUTRITION.route) {
                    NutritionScreen()
                }
                composable(Destination.SETTINGS.route) {
                    ProfileScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBF7FF)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Home Screen",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF1A1A1A)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "App is running normally",
                color = Color(0xFF444444)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Log", "Search", "Chart")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBF7FF))
            .padding(16.dp)
    ) {
        Text(
            text = "Nutrition Tracking",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TabRow(selectedTabIndex = selectedTab) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTab) {
            0 -> NutritionLogScreen()
            1 -> FoodSearchScreen()
            2 -> NutritionChartScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionLogScreen() {
    val formatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    var selectedDateMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }

    val nutritionEntries = remember {
        mutableStateListOf<NutritionEntry>().apply {
            addAll(SampleNutritionData.nutritionEntries)
        }
    }

    val selectedDate = formatter.format(Date(selectedDateMillis))
    val filteredEntries = nutritionEntries.filter { it.dateLabel == selectedDate }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Date: $selectedDate")
            Button(onClick = { showDatePicker = true }) {
                Text("Select Date")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Nutrition Record")
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (filteredEntries.isEmpty()) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "No nutrition records for this date.",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                items(filteredEntries) { entry ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(entry.foodName, fontWeight = FontWeight.Bold)
                            Text("Meal: ${entry.mealType}")
                            Text("Calories: ${entry.calories}")
                            Text("Protein: ${entry.protein} g")
                        }
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDateMillis
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedDateMillis =
                            datePickerState.selectedDateMillis ?: selectedDateMillis
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showAddDialog) {
        AddNutritionDialog(
            onDismiss = { showAddDialog = false },
            onSave = { foodName, mealType, calories ->
                val newId = nutritionEntries.size + 1
                nutritionEntries.add(
                    0,
                    NutritionEntry(
                        id = newId,
                        foodName = foodName,
                        mealType = mealType,
                        calories = calories,
                        protein = (calories / 20).coerceAtLeast(1),
                        dateLabel = selectedDate
                    )
                )
                showAddDialog = false
            }
        )
    }
}

@Composable
fun AddNutritionDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, Int) -> Unit
) {
    var foodName by remember { mutableStateOf("") }
    var mealType by remember { mutableStateOf("Breakfast") }
    var calories by remember { mutableStateOf("") }
    var showMenu by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Nutrition Record") },
        text = {
            Column {
                OutlinedTextField(
                    value = foodName,
                    onValueChange = { foodName = it },
                    label = { Text("Food Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { showMenu = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Meal Type: $mealType")
                }

                androidx.compose.material3.DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    listOf("Breakfast", "Lunch", "Dinner", "Snack").forEach { option ->
                        androidx.compose.material3.DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                mealType = option
                                showMenu = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { Text("Calories") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                if (showError) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Please enter food name and calories.",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val caloriesNumber = calories.toIntOrNull()
                    if (foodName.isNotBlank() && caloriesNumber != null) {
                        onSave(foodName, mealType, caloriesNumber)
                    } else {
                        showError = true
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun FoodSearchScreen() {
    var searchText by remember { mutableStateOf("") }

    val filteredFoods = SampleNutritionData.foodItems.filter { food ->
        searchText.isBlank() || food.name.contains(searchText, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search Food") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredFoods) { food ->
                FoodCard(food)
            }
        }
    }
}

@Composable
fun FoodCard(food: FoodItem) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(food.name, fontWeight = FontWeight.Bold)
            Text("Serving: ${food.serving}")
            Text("Calories: ${food.calories}")
            Text("Protein: ${food.protein} g")
            Text("Carbs: ${food.carbs} g")
        }
    }
}

@Composable
fun NutritionChartScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Today's Summary",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Calories: 1540 / 1800")
                LinearProgressIndicator(
                    progress = { 0.86f },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Protein: 54 / 70 g")
                LinearProgressIndicator(
                    progress = { 0.77f },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Water: 5 / 8 cups")
                LinearProgressIndicator(
                    progress = { 0.62f },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Weekly Trend",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(SampleNutritionData.weeklyCalories) { item ->
                WeeklyProgressCard(item)
            }
        }
    }
}

@Composable
fun WeeklyProgressCard(item: WeeklyNutrition) {
    val progress = item.calories / 1800f

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("${item.dayLabel}: ${item.calories} kcal")
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

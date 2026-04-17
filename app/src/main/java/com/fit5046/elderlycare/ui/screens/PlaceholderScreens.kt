package com.fit5046.elderlycare.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomePlaceholderScreen(
    onOpenNutrition: () -> Unit,
    onOpenSearch: () -> Unit
) {
    PlaceholderScreen(
        title = "Home Dashboard",
        message = "This placeholder keeps the four-tab structure ready for the full group project. Use the buttons below to preview the nutrition module."
    ) {
        Button(onClick = onOpenNutrition, modifier = Modifier.fillMaxWidth()) {
            Text("Open Nutrition Log")
        }
        Button(onClick = onOpenSearch, modifier = Modifier.fillMaxWidth()) {
            Text("Open Food Search")
        }
    }
}

@Composable
fun MedicationPlaceholderScreen() {
    PlaceholderScreen(
        title = "Medication Module",
        message = "Medication list, reminder cards, and add or edit forms can be added here later by your teammate."
    )
}

@Composable
fun SettingsPlaceholderScreen() {
    PlaceholderScreen(
        title = "Profile and Settings",
        message = "This placeholder keeps room for profile forms, emergency contacts, and app preferences."
    )
}

@Composable
private fun PlaceholderScreen(
    title: String,
    message: String,
    actions: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall
        )
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = message, style = MaterialTheme.typography.bodyLarge)
                actions?.invoke()
            }
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Assignment note", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "The prototype only needs screen structure, navigation flow, and visible Android components. Business logic can be added later in Assessment 4."
                )
            }
        }
    }
}


package com.fit5046.elderlycare.ui.screens.nutrition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fit5046.elderlycare.data.SampleNutritionData

@Composable
fun FoodSearchScreen(contentPadding: PaddingValues) {
    var query by rememberSaveable { mutableStateOf("") }
    val foods = remember(query) {
        SampleNutritionData.foodItems.filter {
            query.isBlank() || it.name.contains(query, ignoreCase = true)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = contentPadding.calculateLeftPadding(androidx.compose.ui.unit.LayoutDirection.Ltr),
            top = contentPadding.calculateTopPadding(),
            end = contentPadding.calculateRightPadding(androidx.compose.ui.unit.LayoutDirection.Ltr),
            bottom = 32.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Food search", style = MaterialTheme.typography.titleLarge)
                    Text(
                        "This simple version uses mock food items now. In the full project, a Retrofit API can replace this list."
                    )
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Search food") },
                        placeholder = { Text("Type apple, rice, milk...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null)
                        }
                    )
                }
            }
        }

        if (foods.isEmpty()) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "No food matched your search.",
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }
        } else {
            items(foods, key = { it.id }) { food ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(food.name, style = MaterialTheme.typography.titleMedium)
                        Text("Serving: ${food.serving}")
                        Text("Calories: ${food.calories} kcal")
                        Text("Protein: ${food.protein} g")
                        Text("Carbs: ${food.carbs} g")
                        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                            Text("Add to nutrition log")
                        }
                    }
                }
            }
        }
    }
}


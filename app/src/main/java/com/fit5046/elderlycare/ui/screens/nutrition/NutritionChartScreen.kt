package com.fit5046.elderlycare.ui.screens.nutrition

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fit5046.elderlycare.data.SampleNutritionData

@Composable
fun NutritionChartScreen(contentPadding: PaddingValues) {
    LazyColumn(
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
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Nutrition summary", style = MaterialTheme.typography.titleLarge)
                    ProgressSummaryCard("Calories", "1,540 / 1,800 kcal", 0.86f)
                    ProgressSummaryCard("Protein", "54 / 70 g", 0.77f)
                    ProgressSummaryCard("Water", "5 / 8 cups", 0.62f)
                }
            }
        }
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Weekly calorie trend", style = MaterialTheme.typography.titleMedium)
                    SimpleWeeklyBarChart()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SampleNutritionData.weeklyCalories.forEach { day ->
                            Text(day.dayLabel, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Text(
                        text = "This lightweight chart is enough for the assignment prototype and can be replaced with a richer chart library later.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun ProgressSummaryCard(
    label: String,
    value: String,
    progress: Float
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = MaterialTheme.typography.titleMedium)
        Text(value)
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SimpleWeeklyBarChart() {
    val maxValue = SampleNutritionData.weeklyCalories.maxOf { it.calories }.toFloat()
    val barColor = MaterialTheme.colorScheme.primary
    val guideColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
    ) {
        val widthPerBar = size.width / (SampleNutritionData.weeklyCalories.size * 1.5f)
        val gap = widthPerBar / 2f

        repeat(4) { index ->
            val y = size.height - (index + 1) * (size.height / 5f)
            drawLine(
                color = guideColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 2f
            )
        }

        SampleNutritionData.weeklyCalories.forEachIndexed { index, item ->
            val left = index * (widthPerBar + gap) + gap / 2f
            val barHeight = (item.calories / maxValue) * (size.height * 0.85f)
            drawRoundRect(
                color = barColor,
                topLeft = Offset(left, size.height - barHeight),
                size = Size(widthPerBar, barHeight),
                cornerRadius = CornerRadius(20f, 20f)
            )
        }
    }
}

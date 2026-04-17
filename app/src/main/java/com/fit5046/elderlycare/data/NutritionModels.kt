package com.fit5046.elderlycare.data

data class NutritionEntry(
    val id: Int,
    val foodName: String,
    val mealType: String,
    val calories: Int,
    val protein: Int,
    val dateLabel: String
)

data class FoodItem(
    val id: Int,
    val name: String,
    val serving: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int
)

data class WeeklyNutrition(
    val dayLabel: String,
    val calories: Int
)


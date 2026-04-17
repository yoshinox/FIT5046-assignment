package com.fit5046.elderlycare.data

object SampleNutritionData {
    val nutritionEntries = listOf(
        NutritionEntry(1, "Oatmeal with banana", "Breakfast", 320, 12, "17 Apr 2026"),
        NutritionEntry(2, "Chicken soup", "Lunch", 420, 28, "17 Apr 2026"),
        NutritionEntry(3, "Greek yogurt", "Snack", 160, 14, "17 Apr 2026"),
        NutritionEntry(4, "Rice and steamed fish", "Dinner", 500, 32, "16 Apr 2026"),
        NutritionEntry(5, "Soft tofu pudding", "Snack", 140, 9, "16 Apr 2026")
    )

    val foodItems = listOf(
        FoodItem(1, "Apple", "1 medium", 95, 0, 25),
        FoodItem(2, "Boiled Egg", "1 egg", 78, 6, 1),
        FoodItem(3, "Brown Rice", "1 cup", 216, 5, 45),
        FoodItem(4, "Salmon", "100 g", 208, 20, 0),
        FoodItem(5, "Milk", "1 cup", 122, 8, 12),
        FoodItem(6, "Avocado", "1/2 fruit", 120, 2, 6)
    )

    val weeklyCalories = listOf(
        WeeklyNutrition("Mon", 1540),
        WeeklyNutrition("Tue", 1660),
        WeeklyNutrition("Wed", 1430),
        WeeklyNutrition("Thu", 1710),
        WeeklyNutrition("Fri", 1580),
        WeeklyNutrition("Sat", 1490),
        WeeklyNutrition("Sun", 1620)
    )
}


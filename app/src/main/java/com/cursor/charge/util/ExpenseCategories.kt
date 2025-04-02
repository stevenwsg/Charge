package com.cursor.charge.util

import android.graphics.Color

object ExpenseCategories {
    val HOUSING = "Housing"
    val FOOD = "Food"
    val TRANSPORTATION = "Transportation"
    val ENTERTAINMENT = "Entertainment"
    val SHOPPING = "Shopping"
    val UTILITIES = "Utilities"
    val HEALTH = "Health"
    val EDUCATION = "Education"
    val TRAVEL = "Travel"
    val OTHERS = "Others"
    
    val ALL_CATEGORIES = listOf(
        HOUSING,
        FOOD,
        TRANSPORTATION,
        ENTERTAINMENT,
        SHOPPING,
        UTILITIES,
        HEALTH,
        EDUCATION,
        TRAVEL,
        OTHERS
    )
    
    // Category colors for charts
    val CATEGORY_COLORS = mapOf(
        HOUSING to Color.parseColor("#4285F4"),        // Blue
        FOOD to Color.parseColor("#EA4335"),           // Red
        TRANSPORTATION to Color.parseColor("#FBBC05"), // Yellow
        ENTERTAINMENT to Color.parseColor("#34A853"),  // Green
        SHOPPING to Color.parseColor("#FF6D00"),       // Orange
        UTILITIES to Color.parseColor("#AA00FF"),      // Purple
        HEALTH to Color.parseColor("#00BCD4"),         // Cyan
        EDUCATION to Color.parseColor("#3F51B5"),      // Indigo
        TRAVEL to Color.parseColor("#009688"),         // Teal
        OTHERS to Color.parseColor("#9E9E9E")          // Grey
    )
    
    // Category icons (using material design icon names)
    val CATEGORY_ICONS = mapOf(
        HOUSING to "home",
        FOOD to "restaurant",
        TRANSPORTATION to "directions_car",
        ENTERTAINMENT to "movie",
        SHOPPING to "shopping_cart",
        UTILITIES to "power",
        HEALTH to "healing",
        EDUCATION to "school",
        TRAVEL to "flight",
        OTHERS to "more_horiz"
    )
} 
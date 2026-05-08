package com.sigolaev.lab3.model

data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: String,
    val instructions: String,
    val cookingTimeMinutes: Int,
    val difficulty: Difficulty = Difficulty.EASY
)

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD
}

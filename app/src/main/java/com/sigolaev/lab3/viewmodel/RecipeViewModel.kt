package com.sigolaev.lab3.viewmodel

import androidx.lifecycle.ViewModel
import com.sigolaev.lab3.model.Difficulty
import com.sigolaev.lab3.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RecipeViewModel : ViewModel() {

    private var nextId = 4

    private val _recipes = MutableStateFlow(sampleRecipes())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _visibleRecipes = MutableStateFlow(_recipes.value)
    val recipes: StateFlow<List<Recipe>> = _visibleRecipes

    private val _recipeCount = MutableStateFlow(_recipes.value.size)
    val recipeCount: StateFlow<Int> = _recipeCount

    fun getRecipe(id: Int): Recipe? = _recipes.value.firstOrNull { it.id == id }

    fun addRecipe(
        title: String,
        ingredients: String,
        instructions: String,
        cookingTimeMinutes: Int,
        difficulty: Difficulty
    ) {
        val recipe = Recipe(
            id = nextId++,
            title = title,
            ingredients = ingredients,
            instructions = instructions,
            cookingTimeMinutes = cookingTimeMinutes,
            difficulty = difficulty
        )
        _recipes.value = listOf(recipe) + _recipes.value
        publishState()
    }

    fun updateRecipe(
        id: Int,
        title: String,
        ingredients: String,
        instructions: String,
        cookingTimeMinutes: Int,
        difficulty: Difficulty
    ) {
        _recipes.value = _recipes.value.map { recipe ->
            if (recipe.id == id) {
                recipe.copy(
                    title = title,
                    ingredients = ingredients,
                    instructions = instructions,
                    cookingTimeMinutes = cookingTimeMinutes,
                    difficulty = difficulty
                )
            } else {
                recipe
            }
        }
        publishState()
    }

    fun updateDifficulty(id: Int, difficulty: Difficulty) {
        _recipes.value = _recipes.value.map { recipe ->
            if (recipe.id == id) recipe.copy(difficulty = difficulty) else recipe
        }
        publishState()
    }

    fun deleteRecipe(id: Int) {
        _recipes.value = _recipes.value.filter { it.id != id }
        publishState()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        publishState()
    }

    private fun publishState() {
        _recipeCount.value = _recipes.value.size
        _visibleRecipes.value = if (_searchQuery.value.isBlank()) {
            _recipes.value
        } else {
            _recipes.value.filter { recipe ->
                recipe.title.contains(_searchQuery.value, ignoreCase = true) ||
                    recipe.ingredients.contains(_searchQuery.value, ignoreCase = true)
            }
        }
    }

    private fun sampleRecipes(): List<Recipe> = listOf(
        Recipe(
            id = 1,
            title = "Lemon Herb Pasta",
            ingredients = "Pasta\nLemon\nParsley\nGarlic\nOlive oil\nParmesan",
            instructions = "Boil the pasta until al dente. Warm olive oil with garlic, then toss with pasta, lemon juice, parsley, and parmesan.",
            cookingTimeMinutes = 25,
            difficulty = Difficulty.EASY
        ),
        Recipe(
            id = 2,
            title = "Tomato Rice Skillet",
            ingredients = "Rice\nTomatoes\nOnion\nPaprika\nVegetable broth\nFresh basil",
            instructions = "Saute onion with paprika, add rice and tomatoes, then simmer with broth until the rice is tender. Finish with basil.",
            cookingTimeMinutes = 35,
            difficulty = Difficulty.MEDIUM
        ),
        Recipe(
            id = 3,
            title = "Apple Cinnamon Oats",
            ingredients = "Rolled oats\nApple\nMilk\nCinnamon\nHoney\nWalnuts",
            instructions = "Cook oats with milk and cinnamon. Fold in diced apple, sweeten with honey, and top with walnuts.",
            cookingTimeMinutes = 12,
            difficulty = Difficulty.EASY
        )
    )
}

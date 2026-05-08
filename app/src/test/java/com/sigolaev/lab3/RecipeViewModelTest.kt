package com.sigolaev.lab3

import com.sigolaev.lab3.model.Difficulty
import com.sigolaev.lab3.viewmodel.RecipeViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class RecipeViewModelTest {

    @Test
    fun addRecipe_increasesRecipeCount() {
        val viewModel = RecipeViewModel()
        val initialCount = viewModel.recipeCount.value

        viewModel.addRecipe(
            title = "Vegetable Soup",
            ingredients = "Carrot\nPotato\nOnion",
            instructions = "Cook everything until tender.",
            cookingTimeMinutes = 40,
            difficulty = Difficulty.EASY
        )

        assertEquals(initialCount + 1, viewModel.recipeCount.value)
    }

    @Test
    fun deleteRecipe_removesRecipe() {
        val viewModel = RecipeViewModel()
        val recipeId = viewModel.recipes.value.first().id

        viewModel.deleteRecipe(recipeId)

        assertNull(viewModel.getRecipe(recipeId))
    }

    @Test
    fun updateDifficulty_changesRecipeDifficulty() {
        val viewModel = RecipeViewModel()
        val recipeId = viewModel.recipes.value.first().id

        viewModel.updateDifficulty(recipeId, Difficulty.HARD)

        val updatedRecipe = viewModel.getRecipe(recipeId)
        assertNotNull(updatedRecipe)
        assertEquals(Difficulty.HARD, updatedRecipe?.difficulty)
    }
}

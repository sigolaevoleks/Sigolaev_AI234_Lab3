package com.sigolaev.lab3.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sigolaev.lab3.viewmodel.RecipeViewModel

private object RecipeRoutes {
    const val LIST = "recipes"
    const val ADD = "add?recipeId={recipeId}"
    const val DETAILS = "details/{recipeId}"

    fun add(recipeId: Int? = null): String = recipeId?.let { "add?recipeId=$it" } ?: "add"
    fun details(recipeId: Int): String = "details/$recipeId"
}

@Composable
fun RecipeNavHost(viewModel: RecipeViewModel) {
    val navController = rememberNavController()
    val recipes by viewModel.recipes.collectAsStateWithLifecycle()
    val recipeCount by viewModel.recipeCount.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = RecipeRoutes.LIST
    ) {
        composable(RecipeRoutes.LIST) {
            RecipesListScreen(
                recipes = recipes,
                recipeCount = recipeCount,
                searchQuery = searchQuery,
                onSearchQueryChange = viewModel::updateSearchQuery,
                onAddRecipe = { navController.navigate(RecipeRoutes.add()) },
                onOpenRecipe = { recipeId -> navController.navigate(RecipeRoutes.details(recipeId)) }
            )
        }

        composable(
            route = RecipeRoutes.ADD,
            arguments = listOf(
                navArgument("recipeId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: -1
            val existingRecipe = recipeId.takeIf { it > 0 }?.let(viewModel::getRecipe)
            AddRecipeScreen(
                recipe = existingRecipe,
                onSave = { title, ingredients, instructions, cookingTime, difficulty ->
                    if (existingRecipe == null) {
                        viewModel.addRecipe(title, ingredients, instructions, cookingTime, difficulty)
                    } else {
                        viewModel.updateRecipe(
                            existingRecipe.id,
                            title,
                            ingredients,
                            instructions,
                            cookingTime,
                            difficulty
                        )
                    }
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }

        composable(
            route = RecipeRoutes.DETAILS,
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: -1
            val recipe = viewModel.getRecipe(recipeId)
            DetailsRecipeScreen(
                recipe = recipe,
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate(RecipeRoutes.add(recipeId)) },
                onDelete = {
                    viewModel.deleteRecipe(recipeId)
                    navController.popBackStack(RecipeRoutes.LIST, inclusive = false)
                },
                onDifficultyChange = { difficulty ->
                    viewModel.updateDifficulty(recipeId, difficulty)
                }
            )
        }
    }
}

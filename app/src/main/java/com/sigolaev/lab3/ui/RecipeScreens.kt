package com.sigolaev.lab3.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sigolaev.lab3.R
import com.sigolaev.lab3.model.Difficulty
import com.sigolaev.lab3.model.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesListScreen(
    recipes: List<Recipe>,
    recipeCount: Int,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onAddRecipe: () -> Unit,
    onOpenRecipe: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.screen_title)) },
                actions = {
                    Text(
                        text = pluralStringResource(R.plurals.recipe_count, recipeCount, recipeCount),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddRecipe) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_recipe))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text(stringResource(R.string.search_placeholder)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(28.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            if (recipes.isEmpty()) {
                EmptyRecipesState(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 88.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(recipes, key = { it.id }) { recipe ->
                        RecipeCard(
                            recipe = recipe,
                            onClick = { onOpenRecipe(recipe.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyRecipesState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Restaurant,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(52.dp)
            )
            Text(
                text = stringResource(R.string.empty_title),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.empty_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun RecipeCard(
    recipe: Recipe,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalDining,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = recipe.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = pluralStringResource(
                                R.plurals.cooking_minutes,
                                recipe.cookingTimeMinutes,
                                recipe.cookingTimeMinutes
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                DifficultyBadge(recipe.difficulty)
            }
            Text(
                text = recipe.ingredients.lineSequence().take(3).joinToString(", "),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    recipe: Recipe?,
    onSave: (String, String, String, Int, Difficulty) -> Unit,
    onCancel: () -> Unit
) {
    var title by rememberSaveable(recipe?.id) { mutableStateOf(recipe?.title.orEmpty()) }
    var ingredients by rememberSaveable(recipe?.id) { mutableStateOf(recipe?.ingredients.orEmpty()) }
    var instructions by rememberSaveable(recipe?.id) { mutableStateOf(recipe?.instructions.orEmpty()) }
    var cookingTime by rememberSaveable(recipe?.id) {
        mutableStateOf(recipe?.cookingTimeMinutes?.toString().orEmpty())
    }
    var difficulty by rememberSaveable(recipe?.id) { mutableStateOf(recipe?.difficulty ?: Difficulty.EASY) }
    val parsedTime = cookingTime.toIntOrNull()
    val canSave = title.isNotBlank() &&
        ingredients.isNotBlank() &&
        instructions.isNotBlank() &&
        parsedTime != null &&
        parsedTime > 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(if (recipe == null) R.string.new_recipe_heading else R.string.edit_recipe_heading))
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.recipe_title_label)) },
                placeholder = { Text(stringResource(R.string.recipe_title_placeholder)) },
                singleLine = true
            )
            OutlinedTextField(
                value = ingredients,
                onValueChange = { ingredients = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.ingredients_label)) },
                placeholder = { Text(stringResource(R.string.ingredients_placeholder)) },
                minLines = 4,
                maxLines = 7
            )
            OutlinedTextField(
                value = instructions,
                onValueChange = { instructions = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.instructions_label)) },
                placeholder = { Text(stringResource(R.string.instructions_placeholder)) },
                minLines = 5,
                maxLines = 9
            )
            OutlinedTextField(
                value = cookingTime,
                onValueChange = { value -> cookingTime = value.filter(Char::isDigit) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.time_label)) },
                placeholder = { Text(stringResource(R.string.time_placeholder)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            DifficultySelector(
                selectedDifficulty = difficulty,
                onDifficultySelected = { difficulty = it }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.cancel))
                }
                Button(
                    onClick = {
                        onSave(
                            title.trim(),
                            ingredients.trim(),
                            instructions.trim(),
                            parsedTime ?: 1,
                            difficulty
                        )
                    },
                    enabled = canSave,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.save_recipe))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsRecipeScreen(
    recipe: Recipe?,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDifficultyChange: (Difficulty) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.details_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        }
    ) { innerPadding ->
        if (recipe == null) {
            EmptyMissingRecipe(
                onBack = onBack,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = recipe.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = pluralStringResource(
                                R.plurals.cooking_minutes,
                                recipe.cookingTimeMinutes,
                                recipe.cookingTimeMinutes
                            ),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        DifficultyBadge(recipe.difficulty)
                    }
                }

                RecipeSection(
                    title = stringResource(R.string.ingredients_title),
                    body = recipe.ingredients
                )
                RecipeSection(
                    title = stringResource(R.string.instructions_title),
                    body = recipe.instructions
                )
                DifficultySelector(
                    selectedDifficulty = recipe.difficulty,
                    onDifficultySelected = onDifficultyChange
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.back))
                    }
                    FilledTonalButton(
                        onClick = onEdit,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.edit_recipe))
                    }
                }
                Button(
                    onClick = onDelete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.delete_recipe))
                }
            }
        }
    }
}

@Composable
private fun RecipeSection(
    title: String,
    body: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = body,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun EmptyMissingRecipe(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(R.string.not_found_title),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.not_found_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            TextButton(onClick = onBack) {
                Text(stringResource(R.string.back))
            }
        }
    }
}

@Composable
private fun DifficultySelector(
    selectedDifficulty: Difficulty,
    onDifficultySelected: (Difficulty) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.difficulty_title),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Difficulty.entries.forEach { difficulty ->
                FilterChip(
                    selected = selectedDifficulty == difficulty,
                    onClick = { onDifficultySelected(difficulty) },
                    label = { Text(difficulty.label()) }
                )
            }
        }
    }
}

@Composable
private fun DifficultyBadge(difficulty: Difficulty) {
    val containerColor = when (difficulty) {
        Difficulty.EASY -> MaterialTheme.colorScheme.primaryContainer
        Difficulty.MEDIUM -> MaterialTheme.colorScheme.tertiaryContainer
        Difficulty.HARD -> MaterialTheme.colorScheme.errorContainer
    }
    val contentColor = when (difficulty) {
        Difficulty.EASY -> MaterialTheme.colorScheme.onPrimaryContainer
        Difficulty.MEDIUM -> MaterialTheme.colorScheme.onTertiaryContainer
        Difficulty.HARD -> MaterialTheme.colorScheme.onErrorContainer
    }
    Text(
        text = difficulty.label(),
        style = MaterialTheme.typography.labelMedium,
        color = contentColor,
        modifier = Modifier
            .background(containerColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    )
}

@Composable
private fun Difficulty.label(): String = when (this) {
    Difficulty.EASY -> stringResource(R.string.difficulty_easy)
    Difficulty.MEDIUM -> stringResource(R.string.difficulty_medium)
    Difficulty.HARD -> stringResource(R.string.difficulty_hard)
}

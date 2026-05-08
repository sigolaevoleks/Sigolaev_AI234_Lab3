package com.sigolaev.lab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.sigolaev.lab3.ui.RecipeNavHost
import com.sigolaev.lab3.ui.theme.Sigolaev_AI234_Lab3Theme
import com.sigolaev.lab3.viewmodel.RecipeViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Sigolaev_AI234_Lab3Theme {
                RecipeNavHost(viewModel = viewModel)
            }
        }
    }
}

package com.example.food_ordering

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.food_ordering.ui.theme.Food_OrderingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val viewModel: FoodViewModel = viewModel()

            val darkTheme = when (viewModel.themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            Food_OrderingTheme(darkTheme = darkTheme, dynamicColor = viewModel.dynamicColor) {
                FoodOrderApp(viewModel = viewModel)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodOrderAppPreview() {
    Food_OrderingTheme {
        FoodOrderApp(viewModel = FoodViewModel())
    }
}

@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.food_ordering.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.food_ordering.FoodViewModel
import com.example.food_ordering.ui.components.EmptyState
import com.example.food_ordering.ui.components.FoodCard

@Composable
fun FavoritesScreen(
    viewModel: FoodViewModel,
    onFoodClick: (Int) -> Unit
) {
    val favorites = viewModel.foods.filter { viewModel.isFavorite(it.id) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Favorit") }) }
    ) { innerPadding ->
        if (favorites.isEmpty()) {
            EmptyState(
                emoji = "💜",
                title = "Belum ada favorit",
                message = "Tekan ikon hati di halaman detail untuk menyimpan makanan favoritmu.",
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favorites, key = { it.id }) { food ->
                    FoodCard(food = food, onClick = { onFoodClick(food.id) })
                }
            }
        }
    }
}

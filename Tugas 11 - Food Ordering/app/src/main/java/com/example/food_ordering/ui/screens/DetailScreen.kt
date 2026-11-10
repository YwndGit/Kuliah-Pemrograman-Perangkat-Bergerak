@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.food_ordering.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.food_ordering.FoodViewModel
import com.example.food_ordering.data.Food
import com.example.food_ordering.data.formatRupiah
import com.example.food_ordering.ui.components.BackButton
import com.example.food_ordering.ui.components.FoodImage
import com.example.food_ordering.ui.components.QuantitySelector

@Composable
fun DetailScreen(
    foodId: Int,
    viewModel: FoodViewModel,
    onBack: () -> Unit,
    onCartClick: () -> Unit
) {
    val food = viewModel.getFood(foodId) ?: return

    // State lokal: jumlah porsi & dialog konfirmasi
    var quantity by rememberSaveable { mutableIntStateOf(1) }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val isFavorite = viewModel.isFavorite(food.id)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Menu") },
                navigationIcon = { BackButton(onClick = onBack) },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite(food.id) }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = if (isFavorite) "Hapus dari favorit" else "Tambah ke favorit",
                            tint = if (isFavorite) MaterialTheme.colorScheme.tertiary else LocalContentColor.current
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        QuantitySelector(
                            quantity = quantity,
                            onDecrease = { if (quantity > 1) quantity-- },
                            onIncrease = { quantity++ }
                        )
                        Text(
                            text = formatRupiah(food.price * quantity),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = {
                            viewModel.addToCart(food, quantity)
                            showDialog = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Tambah ke Keranjang")
                    }
                }
            }
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (maxWidth >= 600.dp) {
                // Layar lebar: gambar di kiri, info di kanan
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    FoodImage(
                        emoji = food.emoji,
                        fontSize = 120.sp,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                    FoodInfo(
                        food = food,
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    )
                }
            } else {
                // HP: gambar di atas, info di bawah
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    FoodImage(
                        emoji = food.emoji,
                        fontSize = 100.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    FoodInfo(food = food)
                }
            }
        }
    }

    // Dialog setelah Add to Cart
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = { Text("Berhasil Ditambahkan!") },
            text = { Text("${food.name} sebanyak $quantity porsi telah ditambahkan ke keranjang.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        onCartClick()
                    }
                ) { Text("Lihat Keranjang") }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        onBack()
                    }
                ) { Text("Lanjut Belanja") }
            }
        )
    }
}

@Composable
private fun FoodInfo(food: Food, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = food.name, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(4.dp))
        Text(
            text = "⭐ ${food.rating}  •  ${food.category}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = formatRupiah(food.price),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(16.dp))
        Text(text = "Deskripsi", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))
        Text(text = food.description, style = MaterialTheme.typography.bodyLarge)
    }
}

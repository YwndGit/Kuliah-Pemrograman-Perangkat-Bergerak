@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.food_ordering.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.food_ordering.FoodViewModel
import com.example.food_ordering.data.CartItem
import com.example.food_ordering.data.formatRupiah
import com.example.food_ordering.ui.components.BackButton
import com.example.food_ordering.ui.components.EmptyState
import com.example.food_ordering.ui.components.FoodImage
import com.example.food_ordering.ui.components.PriceRow
import com.example.food_ordering.ui.components.QuantitySelector

@Composable
fun CartScreen(
    viewModel: FoodViewModel,
    onBack: () -> Unit,
    onCheckout: () -> Unit
) {
    val cart = viewModel.cart

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Keranjang") },
                navigationIcon = { BackButton(onClick = onBack) }
            )
        },
        bottomBar = {
            if (cart.isNotEmpty()) {
                Surface(shadowElevation = 8.dp) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        PriceRow("Subtotal", viewModel.subtotal)
                        PriceRow("Ongkos Kirim", viewModel.shippingCost)
                        HorizontalDivider(Modifier.padding(vertical = 8.dp))
                        PriceRow("Total", viewModel.total, isTotal = true)
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = onCheckout, modifier = Modifier.fillMaxWidth()) {
                            Text("Checkout")
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (cart.isEmpty()) {
            EmptyState(
                emoji = "🛒",
                title = "Keranjang masih kosong",
                message = "Yuk, pilih makanan favoritmu dulu!",
                buttonText = "Mulai Belanja",
                onButtonClick = onBack,
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
                items(cart, key = { it.food.id }) { item ->
                    CartItemCard(
                        item = item,
                        onDecrease = { viewModel.updateQuantity(item.food.id, item.quantity - 1) },
                        onIncrease = { viewModel.updateQuantity(item.food.id, item.quantity + 1) },
                        onRemove = { viewModel.removeFromCart(item.food.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CartItemCard(
    item: CartItem,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FoodImage(emoji = item.food.emoji, fontSize = 32.sp, modifier = Modifier.size(64.dp))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = item.food.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = formatRupiah(item.food.price),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                QuantitySelector(
                    quantity = item.quantity,
                    onDecrease = onDecrease,
                    onIncrease = onIncrease
                )
            }
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Hapus",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

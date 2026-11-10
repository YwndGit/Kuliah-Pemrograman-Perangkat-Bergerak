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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.food_ordering.FoodViewModel
import com.example.food_ordering.data.Order
import com.example.food_ordering.data.formatRupiah
import com.example.food_ordering.ui.components.EmptyState

@Composable
fun OrdersScreen(viewModel: FoodViewModel) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Pesanan Saya") }) }
    ) { innerPadding ->
        if (viewModel.orders.isEmpty()) {
            EmptyState(
                emoji = "🧾",
                title = "Belum ada pesanan",
                message = "Pesanan yang kamu buat akan muncul di sini.",
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
                items(viewModel.orders, key = { it.id }) { order ->
                    OrderCard(order = order)
                }
            }
        }
    }
}

@Composable
private fun OrderCard(order: Order) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pesanan #${order.id}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                SuggestionChip(onClick = {}, label = { Text("Diproses") })
            }
            Text(
                text = order.items.joinToString { "${it.food.name} x${it.quantity}" },
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Pembayaran: ${order.paymentMethod}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Alamat: ${order.address}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Total: ${formatRupiah(order.total)}",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

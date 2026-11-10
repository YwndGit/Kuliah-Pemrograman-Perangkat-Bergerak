@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.food_ordering.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.food_ordering.FoodViewModel
import com.example.food_ordering.data.formatRupiah
import com.example.food_ordering.data.paymentMethods
import com.example.food_ordering.ui.components.BackButton
import com.example.food_ordering.ui.components.PriceRow

@Composable
fun CheckoutScreen(
    viewModel: FoodViewModel,
    onBack: () -> Unit,
    onOrderPlaced: () -> Unit
) {
    // State lokal form checkout
    var address by rememberSaveable { mutableStateOf("") }
    var addressError by rememberSaveable { mutableStateOf(false) }
    var selectedPayment by rememberSaveable { mutableStateOf(paymentMethods.first()) }
    var showConfirmDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
                navigationIcon = { BackButton(onClick = onBack) }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Button(
                    onClick = {
                        // Validasi: alamat wajib diisi
                        if (address.isBlank()) {
                            addressError = true
                        } else {
                            showConfirmDialog = true
                        }
                    },
                    enabled = viewModel.cart.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Buat Pesanan • ${formatRupiah(viewModel.total)}")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. Alamat pengiriman
            Text("Alamat Pengiriman", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = address,
                onValueChange = {
                    address = it
                    addressError = false
                },
                label = { Text("Alamat lengkap") },
                placeholder = { Text("Contoh: Jl. Pendidikan No. 10, Sidoarjo") },
                leadingIcon = { Icon(Icons.Filled.LocationOn, contentDescription = null) },
                isError = addressError,
                supportingText = { if (addressError) Text("Alamat wajib diisi") },
                modifier = Modifier.fillMaxWidth()
            )

            // 2. Metode pembayaran
            Text("Metode Pembayaran", style = MaterialTheme.typography.titleMedium)
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.selectableGroup()) {
                    paymentMethods.forEach { method ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = method == selectedPayment,
                                    onClick = { selectedPayment = method },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = method == selectedPayment, onClick = null)
                            Spacer(Modifier.width(12.dp))
                            Text(method, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }

            // 3. Ringkasan pesanan
            Text("Ringkasan Pesanan", style = MaterialTheme.typography.titleMedium)
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    viewModel.cart.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "${item.food.name} x${item.quantity}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = formatRupiah(item.food.price * item.quantity),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    HorizontalDivider(Modifier.padding(vertical = 8.dp))
                    PriceRow("Subtotal", viewModel.subtotal)
                    PriceRow("Ongkos Kirim", viewModel.shippingCost)
                    PriceRow("Total Pembayaran", viewModel.total, isTotal = true)
                }
            }
        }
    }

    // Dialog konfirmasi sebelum pesanan dibuat
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Konfirmasi Pesanan") },
            text = {
                Text("Buat pesanan senilai ${formatRupiah(viewModel.total)} dengan pembayaran $selectedPayment?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        viewModel.placeOrder(address, selectedPayment)
                        onOrderPlaced()
                    }
                ) { Text("Ya, Pesan") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) { Text("Batal") }
            }
        )
    }
}

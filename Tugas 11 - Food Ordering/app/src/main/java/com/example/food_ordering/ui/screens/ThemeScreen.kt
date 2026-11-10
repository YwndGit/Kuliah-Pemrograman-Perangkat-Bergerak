@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.food_ordering.ui.screens

import android.os.Build
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
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.food_ordering.FoodViewModel
import com.example.food_ordering.ThemeMode
import com.example.food_ordering.ui.components.BackButton

private data class ThemeOption(val mode: ThemeMode, val label: String, val icon: ImageVector)

private val themeOptions = listOf(
    ThemeOption(ThemeMode.LIGHT, "Light Mode (Terang)", Icons.Filled.LightMode),
    ThemeOption(ThemeMode.DARK, "Dark Mode (Gelap)", Icons.Filled.DarkMode),
    ThemeOption(ThemeMode.SYSTEM, "Ikuti Pengaturan HP", Icons.Filled.Settings)
)

@Composable
fun ThemeScreen(viewModel: FoodViewModel, onBack: () -> Unit) {
    // Warna dinamis (Material You) hanya ada di Android 12 ke atas
    val dynamicColorSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tema Aplikasi") },
                navigationIcon = { BackButton(onClick = onBack) }
            )
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
            Text("Mode Tampilan", style = MaterialTheme.typography.titleMedium)

            Column(
                modifier = Modifier.selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                themeOptions.forEach { option ->
                    val selected = viewModel.themeMode == option.mode
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (selected) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            }
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = selected,
                                    onClick = { viewModel.updateThemeMode(option.mode) },
                                    role = Role.RadioButton
                                )
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(option.icon, contentDescription = null)
                            Spacer(Modifier.width(16.dp))
                            Text(
                                text = option.label,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )
                            RadioButton(selected = selected, onClick = null)
                        }
                    }
                }
            }

            Text("Warna", style = MaterialTheme.typography.titleMedium)

            Card {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("Warna Dinamis (Material You)", style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = if (dynamicColorSupported) {
                                "Warna aplikasi mengikuti wallpaper HP"
                            } else {
                                "Hanya tersedia di Android 12 ke atas"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = viewModel.dynamicColor,
                        onCheckedChange = { viewModel.updateDynamicColor(it) },
                        enabled = dynamicColorSupported
                    )
                }
            }
        }
    }
}

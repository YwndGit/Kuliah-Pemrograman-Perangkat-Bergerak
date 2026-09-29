package com.example.studentmananger.ui.screen

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentmananger.data.model.Student
import com.example.studentmananger.ui.theme.RedDelete
import com.example.studentmananger.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: UiState,
    onSearchQueryChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (Student) -> Unit,
    onDeleteClick: (Student) -> Unit,
    onMenuToggle: (Boolean) -> Unit,
    onRefresh: () -> Unit,
    onAboutClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Student Manager",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    Box {
                        IconButton(onClick = { onMenuToggle(true) }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu Opsional"
                            )
                        }

                        DropdownMenu(
                            expanded = uiState.isMenuExpanded,
                            onDismissRequest = { onMenuToggle(false) }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Refresh") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = null
                                    )
                                },
                                onClick = { onRefresh() }
                            )
                            DropdownMenuItem(
                                text = { Text("Tentang Aplikasi") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null
                                    )
                                },
                                onClick = { onAboutClick() }
                            )
                            DropdownMenuItem(
                                text = { Text("Keluar") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = null
                                    )
                                },
                                onClick = {
                                    onMenuToggle(false)
                                    (context as? Activity)?.finish()
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Mahasiswa",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Cari mahasiswa...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Cari"
                    )
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Hapus Pencarian"
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Jumlah mahasiswa: ${uiState.filteredStudents.size}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (uiState.filteredStudents.isEmpty()) {
                EmptyStateView(
                    isSearching = uiState.searchQuery.isNotEmpty()
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = uiState.filteredStudents,
                        key = { it.id }
                    ) { student ->
                        StudentCardItem(
                            student = student,
                            onEditClick = { onEditClick(student) },
                            onDeleteClick = { onDeleteClick(student) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StudentCardItem(
    student: Student,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.outlineVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = student.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "NIM: ${student.nim}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = student.programStudi,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = onEditClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Data",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Hapus Data",
                    tint = RedDelete,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyStateView(
    isSearching: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isSearching) Icons.Default.Search else Icons.Default.School,
                contentDescription = null,
                modifier = Modifier.size(54.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = if (isSearching) "Mahasiswa tidak ditemukan" else "Belum ada data mahasiswa",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isSearching)
                "Coba gunakan kata kunci pencarian yang berbeda."
            else
                "Tekan tombol + untuk menambahkan mahasiswa pertama.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

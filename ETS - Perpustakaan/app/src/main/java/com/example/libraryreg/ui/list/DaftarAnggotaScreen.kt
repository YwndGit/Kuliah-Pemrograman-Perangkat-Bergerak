package com.example.libraryreg.ui.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.libraryreg.data.Member
import com.example.libraryreg.ui.theme.DarkBluePrimary
import com.example.libraryreg.ui.theme.DeleteRed
import com.example.libraryreg.ui.viewmodel.MemberViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarAnggotaScreen(
    viewModel: MemberViewModel,
    onBack: () -> Unit,
    onNavigateToAdd: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToEdit: (String) -> Unit
) {
    val members by viewModel.members.collectAsState(initial = emptyList())
    val searchQuery by viewModel.searchQuery.collectAsState()

    var memberToDelete by remember { mutableStateOf<Member?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Anggota Perpustakaan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBluePrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAdd,
                containerColor = DarkBluePrimary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Anggota")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Cari nama atau NIM/NIP...") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Cari") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                singleLine = true
            )

            // Content List or Empty State
            if (members.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada data anggota",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(members, key = { it.nimOrNip }) { member ->
                        Card(
                            onClick = { onNavigateToDetail(member.nimOrNip) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = member.fullName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "NIM/NIP: ${member.nimOrNip}",
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Jenis: ${member.membershipType} (${member.studyProgram})",
                                        fontSize = 13.sp,
                                        color = DarkBluePrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    // View / Detail Button
                                    IconButton(onClick = { onNavigateToDetail(member.nimOrNip) }) {
                                        Icon(
                                            imageVector = Icons.Default.Visibility,
                                            contentDescription = "Lihat",
                                            tint = DarkBluePrimary
                                        )
                                    }
                                    // Edit Button
                                    IconButton(onClick = { onNavigateToEdit(member.nimOrNip) }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    // Delete Button
                                    IconButton(onClick = {
                                        memberToDelete = member
                                        showDeleteDialog = true
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Hapus",
                                            tint = DeleteRed
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Delete Confirmation Dialog
        if (showDeleteDialog && memberToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Konfirmasi Hapus") },
                text = { Text("Yakin ingin menghapus data ini?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            memberToDelete?.let { mem ->
                                viewModel.deleteMember(mem) {
                                    showDeleteDialog = false
                                    memberToDelete = null
                                }
                            }
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = DeleteRed)
                    ) {
                        Text("Hapus", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Batal")
                    }
                }
            )
        }
    }
}

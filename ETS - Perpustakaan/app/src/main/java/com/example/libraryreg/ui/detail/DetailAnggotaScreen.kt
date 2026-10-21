package com.example.libraryreg.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
fun DetailAnggotaScreen(
    viewModel: MemberViewModel,
    memberId: String,
    onBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit
) {
    var member by remember { mutableStateOf<Member?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(memberId) {
        viewModel.loadMember(memberId) { loaded ->
            member = loaded
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Anggota", fontWeight = FontWeight.Bold) },
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
        }
    ) { padding ->
        val currentMember = member
        if (currentMember == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DetailItem(label = "Nama Lengkap", value = currentMember.fullName)
                        DetailItem(label = "NIM / NIP", value = currentMember.nimOrNip)
                        DetailItem(label = "Jenis Keanggotaan", value = currentMember.membershipType)
                        DetailItem(label = "Program Studi / Unit", value = currentMember.studyProgram)
                        DetailItem(label = "Email", value = currentMember.email)
                        DetailItem(label = "Nomor Telepon", value = currentMember.phoneNumber)
                        DetailItem(label = "Alamat", value = currentMember.address)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { onNavigateToEdit(currentMember.nimOrNip) },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(containerColor = DarkBluePrimary)
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Edit", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DeleteRed)
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Hapus")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Hapus", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (showDeleteDialog && currentMember != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Konfirmasi Hapus") },
                text = { Text("Yakin ingin menghapus data ini?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteMember(currentMember) {
                                showDeleteDialog = false
                                onBack()
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

@Composable
fun DetailItem(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
    }
}

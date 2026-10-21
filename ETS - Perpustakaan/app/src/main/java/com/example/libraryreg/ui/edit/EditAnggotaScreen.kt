package com.example.libraryreg.ui.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.libraryreg.ui.theme.DarkBluePrimary
import com.example.libraryreg.ui.theme.SuccessGreen
import com.example.libraryreg.ui.viewmodel.MemberViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAnggotaScreen(
    viewModel: MemberViewModel,
    memberId: String,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    val studyPrograms = listOf(
        "Teknik Informatika",
        "Sistem Informasi",
        "Teknik Elektro",
        "Teknik Mesin",
        "Manajemen",
        "Akuntansi",
        "Ilmu Hukum",
        "Administrasi Publik",
        "Unit Perpustakaan",
        "Unit Layanan Akademik"
    )
    val membershipTypes = listOf("Mahasiswa", "Dosen", "Staf")

    var studyProgramExpanded by remember { mutableStateOf(false) }
    var membershipExpanded by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(memberId) {
        viewModel.loadMember(memberId) {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Anggota", fontWeight = FontWeight.Bold) },
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
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (viewModel.successMessage != null) {
                    Surface(
                        color = SuccessGreen.copy(alpha = 0.15.toFloat()),
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = viewModel.successMessage ?: "",
                            color = SuccessGreen,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                // Nama Lengkap
                OutlinedTextField(
                    value = viewModel.fullName,
                    onValueChange = { viewModel.fullName = it },
                    label = { Text("Nama Lengkap") },
                    isError = viewModel.fullNameError != null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                if (viewModel.fullNameError != null) {
                    Text(text = viewModel.fullNameError ?: "", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                // NIM / NIP (Readonly / Disabled because it's Primary Key)
                OutlinedTextField(
                    value = viewModel.nimOrNip,
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text("NIM / NIP (Tidak dapat diubah)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )

                // Program Studi / Unit
                ExposedDropdownMenuBox(
                    expanded = studyProgramExpanded,
                    onExpandedChange = { studyProgramExpanded = !studyProgramExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = viewModel.studyProgram,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Program Studi / Unit") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = studyProgramExpanded) },
                        isError = viewModel.studyProgramError != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, enabled = true),
                        shape = MaterialTheme.shapes.medium
                    )
                    ExposedDropdownMenu(
                        expanded = studyProgramExpanded,
                        onDismissRequest = { studyProgramExpanded = false }
                    ) {
                        studyPrograms.forEach { program ->
                            DropdownMenuItem(
                                text = { Text(program) },
                                onClick = {
                                    viewModel.studyProgram = program
                                    studyProgramExpanded = false
                                }
                            )
                        }
                    }
                }
                if (viewModel.studyProgramError != null) {
                    Text(text = viewModel.studyProgramError ?: "", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                // Email
                OutlinedTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = viewModel.emailError != null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                if (viewModel.emailError != null) {
                    Text(text = viewModel.emailError ?: "", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                // Nomor Telepon
                OutlinedTextField(
                    value = viewModel.phoneNumber,
                    onValueChange = { viewModel.phoneNumber = it },
                    label = { Text("Nomor Telepon") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    isError = viewModel.phoneNumberError != null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                if (viewModel.phoneNumberError != null) {
                    Text(text = viewModel.phoneNumberError ?: "", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                // Alamat
                OutlinedTextField(
                    value = viewModel.address,
                    onValueChange = { viewModel.address = it },
                    label = { Text("Alamat") },
                    minLines = 3,
                    isError = viewModel.addressError != null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                if (viewModel.addressError != null) {
                    Text(text = viewModel.addressError ?: "", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                // Jenis Keanggotaan
                ExposedDropdownMenuBox(
                    expanded = membershipExpanded,
                    onExpandedChange = { membershipExpanded = !membershipExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = viewModel.membershipType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Jenis Keanggotaan") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = membershipExpanded) },
                        isError = viewModel.membershipTypeError != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, enabled = true),
                        shape = MaterialTheme.shapes.medium
                    )
                    ExposedDropdownMenu(
                        expanded = membershipExpanded,
                        onDismissRequest = { membershipExpanded = false }
                    ) {
                        membershipTypes.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type) },
                                onClick = {
                                    viewModel.membershipType = type
                                    membershipExpanded = false
                                }
                            )
                        }
                    }
                }
                if (viewModel.membershipTypeError != null) {
                    Text(text = viewModel.membershipTypeError ?: "", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Save Button
                Button(
                    onClick = {
                        viewModel.updateMember(memberId) {
                            onBack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(containerColor = DarkBluePrimary)
                ) {
                    Text(text = "Simpan Perubahan", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

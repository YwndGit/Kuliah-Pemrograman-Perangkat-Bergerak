package com.example.studentmananger.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.studentmananger.data.model.PROGRAM_STUDI_LIST
import com.example.studentmananger.data.model.Student

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentFormScreen(
    studentToEdit: Student? = null,
    onBack: () -> Unit,
    onSave: (nim: String, name: String, programStudi: String) -> Unit
) {
    val isEditMode = studentToEdit != null
    val title = if (isEditMode) "Edit Mahasiswa" else "Tambah Mahasiswa"

    var nim by remember { mutableStateOf(studentToEdit?.nim ?: "") }
    var name by remember { mutableStateOf(studentToEdit?.name ?: "") }
    var selectedProgramStudi by remember { mutableStateOf(studentToEdit?.programStudi ?: "") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    var nimError by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf(false) }
    var prodiError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "NIM",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = nim,
                    onValueChange = {
                        nim = it
                        if (it.isNotBlank()) nimError = false
                    },
                    placeholder = { Text(text = "Masukkan NIM") },
                    isError = nimError,
                    supportingText = if (nimError) {
                        { Text(text = "NIM tidak boleh kosong") }
                    } else null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Nama",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        if (it.isNotBlank()) nameError = false
                    },
                    placeholder = { Text(text = "Masukkan nama mahasiswa") },
                    isError = nameError,
                    supportingText = if (nameError) {
                        { Text(text = "Nama tidak boleh kosong") }
                    } else null,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Program Studi",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))

                ExposedDropdownMenuBox(
                    expanded = isDropdownExpanded,
                    onExpandedChange = { isDropdownExpanded = !isDropdownExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = if (selectedProgramStudi.isEmpty()) "Pilih program studi" else selectedProgramStudi,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        isError = prodiError,
                        supportingText = if (prodiError) {
                            { Text(text = "Pilih program studi") }
                        } else null,
                        modifier = Modifier
                            .menuAnchor(androidx.compose.material3.ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false }
                    ) {
                        PROGRAM_STUDI_LIST.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(text = option) },
                                onClick = {
                                    selectedProgramStudi = option
                                    isDropdownExpanded = false
                                    prodiError = false
                                }
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Batal")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        nimError = nim.isBlank()
                        nameError = name.isBlank()
                        prodiError = selectedProgramStudi.isBlank()

                        if (!nimError && !nameError && !prodiError) {
                            onSave(nim, name, selectedProgramStudi)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Simpan")
                }
            }
        }
    }
}

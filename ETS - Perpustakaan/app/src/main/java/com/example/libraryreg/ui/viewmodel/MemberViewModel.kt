package com.example.libraryreg.ui.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryreg.data.AppDatabase
import com.example.libraryreg.data.Member
import com.example.libraryreg.data.MemberRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MemberViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MemberRepository

    init {
        val memberDao = AppDatabase.getDatabase(application).memberDao()
        repository = MemberRepository(memberDao)
    }

    // Search query state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val members: StateFlow<List<Member>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                repository.allMembers
            } else {
                repository.searchMembers(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Form fields state
    var fullName by mutableStateOf("")
    var nimOrNip by mutableStateOf("")
    var studyProgram by mutableStateOf("")
    var email by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var address by mutableStateOf("")
    var membershipType by mutableStateOf("")

    // Form error states
    var fullNameError by mutableStateOf<String?>(null)
    var nimOrNipError by mutableStateOf<String?>(null)
    var studyProgramError by mutableStateOf<String?>(null)
    var emailError by mutableStateOf<String?>(null)
    var phoneNumberError by mutableStateOf<String?>(null)
    var addressError by mutableStateOf<String?>(null)
    var membershipTypeError by mutableStateOf<String?>(null)

    // Operation messages / status
    var successMessage by mutableStateOf<String?>(null)

    fun resetForm() {
        fullName = ""
        nimOrNip = ""
        studyProgram = ""
        email = ""
        phoneNumber = ""
        address = ""
        membershipType = ""
        clearErrors()
        successMessage = null
    }

    private fun clearErrors() {
        fullNameError = null
        nimOrNipError = null
        studyProgramError = null
        emailError = null
        phoneNumberError = null
        addressError = null
        membershipTypeError = null
    }

    private fun validateFields(): Boolean {
        clearErrors()
        var isValid = true

        if (fullName.isBlank()) {
            fullNameError = "Nama lengkap wajib diisi"
            isValid = false
        }
        if (nimOrNip.isBlank()) {
            nimOrNipError = "NIM/NIP wajib diisi"
            isValid = false
        } else if (!nimOrNip.all { it.isDigit() }) {
            nimOrNipError = "NIM/NIP hanya boleh berisi angka"
            isValid = false
        }
        if (studyProgram.isBlank()) {
            studyProgramError = "Program studi/unit wajib dipilih"
            isValid = false
        }
        if (email.isBlank()) {
            emailError = "Email wajib diisi"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Format email tidak valid"
            isValid = false
        }
        if (phoneNumber.isBlank()) {
            phoneNumberError = "Nomor telepon wajib diisi"
            isValid = false
        } else if (!phoneNumber.all { it.isDigit() }) {
            phoneNumberError = "Nomor telepon hanya boleh berisi angka"
            isValid = false
        }
        if (address.isBlank()) {
            addressError = "Alamat wajib diisi"
            isValid = false
        }
        if (membershipType.isBlank()) {
            membershipTypeError = "Jenis keanggotaan wajib dipilih"
            isValid = false
        }

        return isValid
    }

    fun registerMember(onSuccess: () -> Unit) {
        if (!validateFields()) return

        viewModelScope.launch {
            // Check duplicate NIM/NIP
            val existing = repository.getMemberById(nimOrNip)
            if (existing != null) {
                nimOrNipError = "NIM/NIP sudah terdaftar di database"
                return@launch
            }

            val member = Member(
                nimOrNip = nimOrNip,
                fullName = fullName,
                studyProgram = studyProgram,
                email = email,
                phoneNumber = phoneNumber,
                address = address,
                membershipType = membershipType
            )
            repository.insertMember(member)
            successMessage = "Pendaftaran berhasil!"
            onSuccess()
        }
    }

    fun loadMember(nimOrNip: String, onLoaded: (Member) -> Unit) {
        viewModelScope.launch {
            val member = repository.getMemberById(nimOrNip)
            if (member != null) {
                fullName = member.fullName
                this@MemberViewModel.nimOrNip = member.nimOrNip
                studyProgram = member.studyProgram
                email = member.email
                phoneNumber = member.phoneNumber
                address = member.address
                membershipType = member.membershipType
                onLoaded(member)
            }
        }
    }

    fun updateMember(originalNimOrNip: String, onSuccess: () -> Unit) {
        clearErrors()
        var isValid = true

        if (fullName.isBlank()) {
            fullNameError = "Nama lengkap wajib diisi"
            isValid = false
        }
        if (studyProgram.isBlank()) {
            studyProgramError = "Program studi/unit wajib dipilih"
            isValid = false
        }
        if (email.isBlank()) {
            emailError = "Email wajib diisi"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Format email tidak valid"
            isValid = false
        }
        if (phoneNumber.isBlank()) {
            phoneNumberError = "Nomor telepon wajib diisi"
            isValid = false
        } else if (!phoneNumber.all { it.isDigit() }) {
            phoneNumberError = "Nomor telepon hanya boleh berisi angka"
            isValid = false
        }
        if (address.isBlank()) {
            addressError = "Alamat wajib diisi"
            isValid = false
        }
        if (membershipType.isBlank()) {
            membershipTypeError = "Jenis keanggotaan wajib dipilih"
            isValid = false
        }

        if (!isValid) return

        viewModelScope.launch {
            val member = Member(
                nimOrNip = originalNimOrNip,
                fullName = fullName,
                studyProgram = studyProgram,
                email = email,
                phoneNumber = phoneNumber,
                address = address,
                membershipType = membershipType
            )
            repository.updateMember(member)
            successMessage = "Perubahan data berhasil disimpan!"
            onSuccess()
        }
    }

    fun deleteMember(member: Member, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.deleteMember(member)
            onSuccess()
        }
    }
}

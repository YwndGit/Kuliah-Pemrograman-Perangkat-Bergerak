package com.example.studentmananger.data.model

data class Student(
    val id: String = java.util.UUID.randomUUID().toString(),
    val nim: String,
    val name: String,
    val programStudi: String
)

val PROGRAM_STUDI_LIST = listOf(
    "Informatika",
    "Sistem Informasi",
    "Teknik Komputer",
    "Desain Komunikasi Visual",
    "Manajemen",
    "Akuntansi",
    "Rekayasa Perangkat Lunak"
)

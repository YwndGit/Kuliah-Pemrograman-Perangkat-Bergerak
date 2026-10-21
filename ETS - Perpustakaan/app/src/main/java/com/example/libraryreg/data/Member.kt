package com.example.libraryreg.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class Member(
    @PrimaryKey
    val nimOrNip: String,
    val fullName: String,
    val studyProgram: String,
    val email: String,
    val phoneNumber: String,
    val address: String,
    val membershipType: String
)

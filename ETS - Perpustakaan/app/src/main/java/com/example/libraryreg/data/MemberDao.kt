package com.example.libraryreg.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Query("SELECT * FROM members ORDER BY fullName ASC")
    fun getAllMembers(): Flow<List<Member>>

    @Query("SELECT * FROM members WHERE nimOrNip = :nimOrNip")
    suspend fun getMemberById(nimOrNip: String): Member?

    @Query("SELECT * FROM members WHERE fullName LIKE '%' || :query || '%' OR nimOrNip LIKE '%' || :query || '%' ORDER BY fullName ASC")
    fun searchMembers(query: String): Flow<List<Member>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMember(member: Member)

    @Update
    suspend fun updateMember(member: Member)

    @Delete
    suspend fun deleteMember(member: Member)

    @Query("DELETE FROM members WHERE nimOrNip = :nimOrNip")
    suspend fun deleteMemberById(nimOrNip: String)
}

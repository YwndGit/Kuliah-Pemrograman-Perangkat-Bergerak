package com.example.libraryreg.data

import kotlinx.coroutines.flow.Flow

class MemberRepository(private val memberDao: MemberDao) {
    val allMembers: Flow<List<Member>> = memberDao.getAllMembers()

    fun searchMembers(query: String): Flow<List<Member>> {
        return memberDao.searchMembers(query)
    }

    suspend fun getMemberById(nimOrNip: String): Member? {
        return memberDao.getMemberById(nimOrNip)
    }

    suspend fun insertMember(member: Member) {
        memberDao.insertMember(member)
    }

    suspend fun updateMember(member: Member) {
        memberDao.updateMember(member)
    }

    suspend fun deleteMember(member: Member) {
        memberDao.deleteMember(member)
    }
}

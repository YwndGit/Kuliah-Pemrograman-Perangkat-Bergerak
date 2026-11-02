package com.example.loginapplication.data.repository

import com.example.loginapplication.data.local.dao.UserDao
import com.example.loginapplication.data.local.entity.User

class UserRepository(private val userDao: UserDao) {
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    suspend fun login(username: String, password: String): User? {
        return userDao.login(username, password)
    }

    suspend fun ensureDummyUser() {
        if (userDao.getUserByUsername("admin") == null) {
            userDao.insert(User(id = 1, username = "admin", password = "12345"))
        }
    }
}

package com.example.loginapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.loginapplication.data.local.database.AppDatabase
import com.example.loginapplication.data.repository.UserRepository
import com.example.loginapplication.ui.screen.LoginScreen
import com.example.loginapplication.ui.theme.LoginApplicationTheme
import com.example.loginapplication.ui.viewmodel.LoginViewModel
import com.example.loginapplication.ui.viewmodel.LoginViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(this)
        val repository = UserRepository(database.userDao())
        val factory = LoginViewModelFactory(repository)

        setContent {
            LoginApplicationTheme {
                val viewModel: LoginViewModel = viewModel(factory = factory)
                LoginScreen(viewModel = viewModel)
            }
        }
    }
}

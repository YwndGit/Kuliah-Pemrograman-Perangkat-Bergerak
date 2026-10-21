package com.example.libraryreg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.libraryreg.navigation.AppNavGraph
import com.example.libraryreg.ui.theme.LibraryRegTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LibraryRegTheme {
                AppNavGraph()
            }
        }
    }
}

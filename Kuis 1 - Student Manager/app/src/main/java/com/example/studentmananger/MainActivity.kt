package com.example.studentmananger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentmananger.ui.screen.AboutDialog
import com.example.studentmananger.ui.screen.DeleteConfirmationDialog
import com.example.studentmananger.ui.screen.HomeScreen
import com.example.studentmananger.ui.screen.SplashScreen
import com.example.studentmananger.ui.screen.StudentFormScreen
import com.example.studentmananger.ui.theme.StudentManangerTheme
import com.example.studentmananger.ui.viewmodel.Screen
import com.example.studentmananger.ui.viewmodel.StudentViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentManangerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StudentManagerApp()
                }
            }
        }
    }
}

@Composable
fun StudentManagerApp(
    viewModel: StudentViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.studentToDelete != null) {
        DeleteConfirmationDialog(
            student = uiState.studentToDelete!!,
            onConfirm = { viewModel.confirmDeleteStudent() },
            onDismiss = { viewModel.dismissDeleteDialog() }
        )
    }

    if (uiState.showAboutDialog) {
        AboutDialog(
            onDismiss = { viewModel.toggleAboutDialog(false) }
        )
    }

    Crossfade(targetState = uiState.currentScreen, label = "ScreenTransition") { screen ->
        when (screen) {
            is Screen.Splash -> {
                SplashScreen(
                    onSplashFinished = { viewModel.navigateTo(Screen.Home) }
                )
            }

            is Screen.Home -> {
                HomeScreen(
                    uiState = uiState,
                    onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                    onAddClick = { viewModel.navigateTo(Screen.AddStudent) },
                    onEditClick = { student -> viewModel.navigateTo(Screen.EditStudent(student)) },
                    onDeleteClick = { student -> viewModel.requestDeleteStudent(student) },
                    onMenuToggle = { expanded -> viewModel.toggleMenu(expanded) },
                    onRefresh = { viewModel.refreshData() },
                    onAboutClick = { viewModel.toggleAboutDialog(true) }
                )
            }

            is Screen.AddStudent -> {
                StudentFormScreen(
                    studentToEdit = null,
                    onBack = { viewModel.navigateTo(Screen.Home) },
                    onSave = { nim, name, prodi ->
                        viewModel.addStudent(nim, name, prodi)
                    }
                )
            }

            is Screen.EditStudent -> {
                StudentFormScreen(
                    studentToEdit = screen.student,
                    onBack = { viewModel.navigateTo(Screen.Home) },
                    onSave = { nim, name, prodi ->
                        viewModel.updateStudent(screen.student.id, nim, name, prodi)
                    }
                )
            }
        }
    }
}

package com.example.studentmananger.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.studentmananger.data.model.Student
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed class Screen {
    data object Splash : Screen()
    data object Home : Screen()
    data object AddStudent : Screen()
    data class EditStudent(val student: Student) : Screen()
}

data class UiState(
    val currentScreen: Screen = Screen.Splash,
    val students: List<Student> = initialStudents,
    val searchQuery: String = "",
    val studentToDelete: Student? = null,
    val showAboutDialog: Boolean = false,
    val isMenuExpanded: Boolean = false
) {
    val filteredStudents: List<Student>
        get() {
            if (searchQuery.isBlank()) return students
            val query = searchQuery.trim().lowercase()
            return students.filter {
                it.name.lowercase().contains(query) ||
                        it.nim.lowercase().contains(query) ||
                        it.programStudi.lowercase().contains(query)
            }
        }
}

private val initialStudents = listOf(
    Student(nim = "5053251019", name = "Nafis", programStudi = "Informatika"),
    Student(nim = "5053251020", name = "Yuwand Arteta", programStudi = "Informatika"),
    Student(nim = "5053251021", name = "Zaidaan", programStudi = "Teknik Komputer")
)

class StudentViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun navigateTo(screen: Screen) {
        _uiState.update { it.copy(currentScreen = screen, isMenuExpanded = false) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun addStudent(nim: String, name: String, programStudi: String) {
        val newStudent = Student(nim = nim.trim(), name = name.trim(), programStudi = programStudi.trim())
        _uiState.update { state ->
            state.copy(
                students = state.students + newStudent,
                currentScreen = Screen.Home
            )
        }
    }

    fun updateStudent(id: String, nim: String, name: String, programStudi: String) {
        _uiState.update { state ->
            val updatedList = state.students.map { student ->
                if (student.id == id) {
                    student.copy(nim = nim.trim(), name = name.trim(), programStudi = programStudi.trim())
                } else {
                    student
                }
            }
            state.copy(
                students = updatedList,
                currentScreen = Screen.Home
            )
        }
    }

    fun requestDeleteStudent(student: Student) {
        _uiState.update { it.copy(studentToDelete = student) }
    }

    fun confirmDeleteStudent() {
        val target = _uiState.value.studentToDelete ?: return
        _uiState.update { state ->
            state.copy(
                students = state.students.filterNot { it.id == target.id },
                studentToDelete = null
            )
        }
    }

    fun dismissDeleteDialog() {
        _uiState.update { it.copy(studentToDelete = null) }
    }

    fun toggleMenu(expanded: Boolean) {
        _uiState.update { it.copy(isMenuExpanded = expanded) }
    }

    fun toggleAboutDialog(show: Boolean) {
        _uiState.update { it.copy(showAboutDialog = show, isMenuExpanded = false) }
    }

    fun refreshData() {
        _uiState.update {
            it.copy(
                students = initialStudents,
                searchQuery = "",
                isMenuExpanded = false
            )
        }
    }
}

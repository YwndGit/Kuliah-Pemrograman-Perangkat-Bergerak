package com.example.libraryreg.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.libraryreg.ui.beranda.BerandaScreen
import com.example.libraryreg.ui.detail.DetailAnggotaScreen
import com.example.libraryreg.ui.edit.EditAnggotaScreen
import com.example.libraryreg.ui.form.FormulirScreen
import com.example.libraryreg.ui.list.DaftarAnggotaScreen
import com.example.libraryreg.ui.viewmodel.MemberViewModel

sealed class Screen(val route: String) {
    object Beranda : Screen("beranda")
    object Formulir : Screen("formulir")
    object Daftar : Screen("daftar")
    object Detail : Screen("detail/{memberId}") {
        fun createRoute(memberId: String) = "detail/$memberId"
    }
    object Edit : Screen("edit/{memberId}") {
        fun createRoute(memberId: String) = "edit/$memberId"
    }
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val viewModel: MemberViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Beranda.route
    ) {
        composable(Screen.Beranda.route) {
            BerandaScreen(
                onNavigateToForm = { navController.navigate(Screen.Formulir.route) },
                onNavigateToList = {
                    viewModel.setSearchQuery("")
                    navController.navigate(Screen.Daftar.route)
                },
                onNavigateToSearch = {
                    viewModel.setSearchQuery("")
                    navController.navigate(Screen.Daftar.route)
                }
            )
        }
        composable(Screen.Formulir.route) {
            FormulirScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Daftar.route) {
            DaftarAnggotaScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToAdd = { navController.navigate(Screen.Formulir.route) },
                onNavigateToDetail = { id -> navController.navigate(Screen.Detail.createRoute(id)) },
                onNavigateToEdit = { id -> navController.navigate(Screen.Edit.createRoute(id)) }
            )
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("memberId") { type = NavType.StringType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getString("memberId") ?: ""
            DetailAnggotaScreen(
                viewModel = viewModel,
                memberId = memberId,
                onBack = { navController.popBackStack() },
                onNavigateToEdit = { id -> navController.navigate(Screen.Edit.createRoute(id)) }
            )
        }
        composable(
            route = Screen.Edit.route,
            arguments = listOf(navArgument("memberId") { type = NavType.StringType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getString("memberId") ?: ""
            EditAnggotaScreen(
                viewModel = viewModel,
                memberId = memberId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

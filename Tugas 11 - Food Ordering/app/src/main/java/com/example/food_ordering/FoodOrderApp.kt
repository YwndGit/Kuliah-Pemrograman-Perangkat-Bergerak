package com.example.food_ordering

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.food_ordering.ui.screens.CartScreen
import com.example.food_ordering.ui.screens.CheckoutScreen
import com.example.food_ordering.ui.screens.DetailScreen
import com.example.food_ordering.ui.screens.FavoritesScreen
import com.example.food_ordering.ui.screens.HomeScreen
import com.example.food_ordering.ui.screens.OrdersScreen
import com.example.food_ordering.ui.screens.ProfileScreen
import com.example.food_ordering.ui.screens.ThemeScreen
import kotlinx.coroutines.launch

// Alamat (route) setiap halaman
object Routes {
    const val HOME = "home"
    const val ORDERS = "orders"
    const val FAVORITES = "favorites"
    const val PROFILE = "profile"
    const val DETAIL = "detail/{foodId}"
    const val CART = "cart"
    const val CHECKOUT = "checkout"
    const val THEME = "theme"

    fun detail(foodId: Int) = "detail/$foodId"
}

// Menu navigasi utama
data class NavItem(val route: String, val label: String, val icon: ImageVector)

val navItems = listOf(
    NavItem(Routes.HOME, "Beranda", Icons.Filled.Home),
    NavItem(Routes.ORDERS, "Pesanan", Icons.Filled.Receipt),
    NavItem(Routes.FAVORITES, "Favorit", Icons.Filled.Favorite),
    NavItem(Routes.PROFILE, "Profil", Icons.Filled.Person)
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FoodOrderApp(viewModel: FoodViewModel) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Halaman yang sedang dibuka
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showNavigation = navItems.any { it.route == currentRoute }

    // Pindah tab tanpa menumpuk halaman
    fun openTab(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    // Tampilkan Snackbar (notifikasi singkat)
    fun showMessage(message: String) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(message)
        }
    }

    BoxWithConstraints {
        // Responsif: layar lebar (tablet/landscape) memakai NavigationRail di samping
        val isWideScreen = maxWidth >= 600.dp

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                if (showNavigation && !isWideScreen) {
                    NavigationBar {
                        navItems.forEach { item ->
                            NavigationBarItem(
                                selected = currentRoute == item.route,
                                onClick = { openTab(item.route) },
                                icon = { Icon(item.icon, contentDescription = item.label) },
                                label = { Text(item.label) }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
            ) {
                if (showNavigation && isWideScreen) {
                    NavigationRail {
                        navItems.forEach { item ->
                            NavigationRailItem(
                                selected = currentRoute == item.route,
                                onClick = { openTab(item.route) },
                                icon = { Icon(item.icon, contentDescription = item.label) },
                                label = { Text(item.label) }
                            )
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = Routes.HOME,
                    modifier = Modifier.weight(1f)
                ) {
                    composable(Routes.HOME) {
                        HomeScreen(
                            viewModel = viewModel,
                            onFoodClick = { id -> navController.navigate(Routes.detail(id)) },
                            onCartClick = { navController.navigate(Routes.CART) },
                            onShowMessage = { message -> showMessage(message) }
                        )
                    }

                    composable(
                        route = Routes.DETAIL,
                        arguments = listOf(navArgument("foodId") { type = NavType.IntType })
                    ) { entry ->
                        val foodId = entry.arguments?.getInt("foodId") ?: 0
                        DetailScreen(
                            foodId = foodId,
                            viewModel = viewModel,
                            onBack = { navController.navigateUp() },
                            onCartClick = { navController.navigate(Routes.CART) }
                        )
                    }

                    composable(Routes.CART) {
                        CartScreen(
                            viewModel = viewModel,
                            onBack = { navController.navigateUp() },
                            onCheckout = { navController.navigate(Routes.CHECKOUT) }
                        )
                    }

                    composable(Routes.CHECKOUT) {
                        CheckoutScreen(
                            viewModel = viewModel,
                            onBack = { navController.navigateUp() },
                            onOrderPlaced = {
                                showMessage("Pesanan berhasil dibuat! 🎉")
                                // Tutup halaman keranjang & checkout, lalu buka tab Pesanan
                                navController.navigate(Routes.ORDERS) {
                                    popUpTo(Routes.HOME)
                                }
                            }
                        )
                    }

                    composable(Routes.ORDERS) {
                        OrdersScreen(viewModel = viewModel)
                    }

                    composable(Routes.FAVORITES) {
                        FavoritesScreen(
                            viewModel = viewModel,
                            onFoodClick = { id -> navController.navigate(Routes.detail(id)) }
                        )
                    }

                    composable(Routes.PROFILE) {
                        ProfileScreen(onThemeClick = { navController.navigate(Routes.THEME) })
                    }

                    composable(Routes.THEME) {
                        ThemeScreen(
                            viewModel = viewModel,
                            onBack = { navController.navigateUp() }
                        )
                    }
                }
            }
        }
    }
}

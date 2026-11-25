package com.example.mobilecomputinproject.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.mobilecomputinproject.viewmodel.ShopViewModel

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Catalog : Screen("catalog", "Shop", Icons.Default.Home)
    object Cart : Screen("cart", "Cart", Icons.Default.ShoppingCart)
    object Orders : Screen("orders", "Orders", Icons.Default.List)
    object Detail : Screen("detail/{productId}", "Detail", Icons.Default.Home)
    object Checkout : Screen("checkout", "Checkout", Icons.Default.Home)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopApp() {
    val navController = rememberNavController()
    val viewModel: ShopViewModel = viewModel()
    val cartItemCount by viewModel.cartItemCount.collectAsState()

    val items = listOf(
        Screen.Catalog,
        Screen.Cart,
        Screen.Orders
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            if (currentRoute == Screen.Catalog.route || currentRoute == Screen.Cart.route || currentRoute == Screen.Orders.route) {
                NavigationBar {
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = {
                                if (screen == Screen.Cart && cartItemCount > 0) {
                                    BadgedBox(
                                        badge = { Badge { Text("$cartItemCount") } }
                                    ) {
                                        Icon(screen.icon, contentDescription = screen.title)
                                    }
                                } else {
                                    Icon(screen.icon, contentDescription = screen.title)
                                }
                            },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        if (screen.route == Screen.Catalog.route) {
                                            popUpTo(Screen.Catalog.route) { inclusive = false }
                                        } else {
                                            popUpTo(Screen.Catalog.route) { saveState = true }
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Catalog.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(Screen.Catalog.route) {
                CatalogScreen(
                    viewModel = viewModel,
                    onProductClick = { productId ->
                        navController.navigate("detail/$productId")
                    }
                )
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                ProductDetailScreen(
                    productId = productId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onAddToCartSuccess = {
                        navController.navigate(Screen.Cart.route)
                    }
                )
            }
            composable(Screen.Cart.route) {
                CartScreen(
                    viewModel = viewModel,
                    onCheckoutClick = {
                        navController.navigate(Screen.Checkout.route)
                    }
                )
            }
            composable(Screen.Checkout.route) {
                CheckoutScreen(
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onOrderPlaced = { orderId ->
                        navController.navigate(Screen.Orders.route) {
                            popUpTo(Screen.Catalog.route)
                        }
                    }
                )
            }
            composable(Screen.Orders.route) {
                OrderHistoryScreen(viewModel = viewModel)
            }
        }
    }
}

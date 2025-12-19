package com.eya.smartshop.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eya.smartshop.auth.AuthViewModel
import com.eya.smartshop.cart.CartViewModel
import com.eya.smartshop.product.ProductViewModel
import com.eya.smartshop.ui.CartScreen
import com.eya.smartshop.ui.LoginScreen
import com.eya.smartshop.ui.ProductListScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    authVm: AuthViewModel,
    productVm: ProductViewModel,
    cartVm: CartViewModel
) {
    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(vm = authVm, onLoginSuccess = {
                navController.navigate("products") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }

        composable("products") {
            ProductListScreen(vm = productVm, cartVm = cartVm, onNavigateToCart = {
                navController.navigate("cart")
            })
        }

        composable("cart") {
            CartScreen(
                cartVm = cartVm,
                productVm = productVm,
                onBack = { navController.popBackStack() }
            )
        }

    }
}
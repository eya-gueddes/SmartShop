package com.eya.smartshop

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.eya.smartshop.auth.AuthRepository
import com.eya.smartshop.auth.AuthViewModel
import com.eya.smartshop.cart.CartViewModel
import com.eya.smartshop.nav.AppNavGraph
import com.eya.smartshop.product.AppDatabase
import com.eya.smartshop.product.ProductRepository
import com.eya.smartshop.product.ProductViewModel
import com.eya.smartshop.ui.theme.SmartShopTheme

class MainActivity : ComponentActivity() {

    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // room db
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app.db"
        ).build()

        setContent {
            SmartShopTheme {
                val navController = rememberNavController()

                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                    "app.db"
                ).build()

                val authVm = AuthViewModel(AuthRepository())
                val productVm = ProductViewModel(ProductRepository(db.productDao()))
                val cartVm = CartViewModel()

                AppNavGraph(
                    navController = navController,
                    authVm = authVm,
                    productVm = productVm,
                    cartVm = cartVm
                )
            }
        }
    }
}

package com.eya.smartshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.room.Room
import com.eya.smartshop.auth.AuthRepository
import com.eya.smartshop.auth.AuthViewModel
import com.eya.smartshop.auth.LoginScreen
import com.eya.smartshop.product.AppDatabase
import com.eya.smartshop.product.ProductRepository
import com.eya.smartshop.product.ProductViewModel
import com.eya.smartshop.ui.ProductListScreen
import com.eya.smartshop.ui.theme.SmartShopTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // room db
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app.db"
        ).build()

        // auth
        val authVm = AuthViewModel(AuthRepository())

        // product
        val productRepo = ProductRepository(db.productDao())
        val productVm = ProductViewModel(productRepo)

        setContent {
            SmartShopTheme {

                // Si user connecté → page produits
                if (authVm.uiState.user != null) {
                    ProductListScreen(productVm)
                } else {
                    LoginScreen(authVm)
                }
            }
        }
    }
}

package com.eya.smartshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eya.smartshop.auth.AuthRepository
import com.eya.smartshop.auth.AuthViewModel
import com.eya.smartshop.auth.LoginScreen
import com.eya.smartshop.ui.theme.SmartShopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val vm = AuthViewModel(AuthRepository())

        setContent {
            SmartShopTheme {
                LoginScreen(vm)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SmartShopTheme {
        Greeting("Android")
    }
}
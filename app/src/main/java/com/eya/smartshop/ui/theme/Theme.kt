package com.eya.smartshop.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext



private val LightColorScheme = lightColorScheme(
    primary = Burgundy,
    onPrimary = Color.White,
    secondary = SandGold,
    background = ChampagneBeige,
    onBackground = DarkMaroon,
    surface = Color.White,
    onSurface = DarkMaroon
)

private val DarkColorScheme = darkColorScheme(
    primary = Burgundy,
    onPrimary = Color.White,
    secondary = SandGold,
    background = DeepWine,
    onBackground = SandGold,
    surface = DarkMaroon,
    onSurface = ChampagneBeige
)

@Composable
fun SmartShopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    useDynamicColor: Boolean = false, // nouvelle option pour contrôler
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

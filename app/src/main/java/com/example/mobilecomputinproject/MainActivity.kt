package com.example.mobilecomputinproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.mobilecomputinproject.ui.screens.ShopApp

val BabyBluePrimary = Color(0xFF90CAF9)
val BabyBlueSecondary = Color(0xFF81C784)
val BabyBlueTertiary = Color(0xFFFFB74D)
val BabyBlueBackground = Color(0xFFF4F9FC)
val BabyBlueSurface = Color(0xFFFFFFFF)
val BabyBlueSurfaceVariant = Color(0xFFEAF4FB)
val BabyBluePrimaryContainer = Color(0xFFE3F2FD)
val BabyBlueOnPrimaryContainer = Color(0xFF0D47A1)
val BabyBlueSecondaryContainer = Color(0xFFD0E8FC)

private val BabyBlueColorScheme = lightColorScheme(
    primary = BabyBluePrimary,
    onPrimary = Color(0xFF0D47A1),
    primaryContainer = BabyBluePrimaryContainer,
    onPrimaryContainer = BabyBlueOnPrimaryContainer,
    secondary = BabyBlueSecondary,
    onSecondary = Color(0xFF1B5E20),
    secondaryContainer = BabyBlueSecondaryContainer,
    onSecondaryContainer = Color(0xFF00325B),
    tertiary = BabyBlueTertiary,
    background = BabyBlueBackground,
    onBackground = Color(0xFF1A1C1E),
    surface = BabyBlueSurface,
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = BabyBlueSurfaceVariant,
    onSurfaceVariant = Color(0xFF42474E)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(colorScheme = BabyBlueColorScheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShopApp()
                }
            }
        }
    }
}

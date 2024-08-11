package com.datasite.mytaxitestapp.screen.compose.myTheme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun DarkTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF80ED99),          // my_green  /
            onPrimary = Color(0xFFFFFFFF),         // my_black /
            primaryContainer = Color(0xFFE8EAF1), // my_line_color   /
            inversePrimary = Color(0xFFF44336),    // my_red  /
            secondary = Color(0xFF4A91FB),         // my_navigation_color  /
            errorContainer = Color(0xE6253246),  // my_transparent  /
            onSecondary = Color(0xFF121212),        // my_white  /
            secondaryContainer = Color(0xFF666666), // my_right_icon /
            tertiary = Color(0xFF999999),          // my_icon_color  /
            tertiaryContainer = Color(0xE6242424), // my_gray  /
        ),
        typography = Typography(),
        content = content
    )
}

package com.datasite.mytaxitestapp.screen.compose.myTheme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun LightTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF80ED99),          // my_green  /
            onPrimary = Color(0xFF121212),         // my_black /
            primaryContainer = Color(0xFFE8EAF1), // my_line_color   /
            inversePrimary = Color(0xFFF44336),    // my_red  /
            secondary = Color(0xFF4A91FB),         // my_navigation_color  /
            errorContainer = Color(0xFFE6FFFFFF),  // my_transparent  /
            onSecondary = Color(0xFFFFFFFF),        // my_white  /
            secondaryContainer = Color(0xFFBBC2D5), // my_right_icon /
            tertiary = Color(0xFF818AB0),          // my_icon_color  /
            tertiaryContainer = Color(0xE6F9F5F6), // my_gray  /
        ),
        typography = Typography(), // You can define custom typography here
        content = content
    )
}
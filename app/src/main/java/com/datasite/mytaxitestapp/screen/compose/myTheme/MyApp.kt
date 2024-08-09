package com.datasite.mytaxitestapp.screen.compose.myTheme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

@Composable
fun MyApp(content: @Composable () -> Unit) {
    val darkTheme = isSystemInDarkTheme()

    if (darkTheme) {
        DarkTheme(content = content)
    } else {
        LightTheme(content = content)
    }
}

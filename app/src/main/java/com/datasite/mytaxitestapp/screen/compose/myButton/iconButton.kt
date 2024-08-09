package com.datasite.mytaxitestapp.screen.compose.myButton

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat


@Composable
fun iconButton(icon : Int, color : Int, iconColor : Int , onClick: () -> Unit){

    IconButton(modifier = Modifier
        .size(56.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(colorResource(id = color), shape = RoundedCornerShape(14.dp)),
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = colorResource(id = iconColor)
        ),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = icon),
            contentDescription = "menu")
    }

}

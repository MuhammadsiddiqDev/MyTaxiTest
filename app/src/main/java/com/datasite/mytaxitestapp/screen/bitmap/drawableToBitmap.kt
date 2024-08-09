package com.datasite.mytaxitestapp.screen.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import androidx.core.content.ContextCompat


fun drawableToBitmap(context: Context, drawableId: Int, width: Int, height: Int): Bitmap {
    val drawable: Drawable? = ContextCompat.getDrawable(context, drawableId)
    if (drawable == null) {
        throw IllegalArgumentException("Drawable resource not found.")
    }

    val bitmap = when (drawable) {
        is BitmapDrawable -> {
            // Scale BitmapDrawable
            Bitmap.createScaledBitmap(drawable.bitmap, width, height, false)
        }
        is VectorDrawable -> {
            // Convert VectorDrawable to Bitmap and scale
            Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
                val canvas = Canvas(this)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
            }
        }
        else -> {
            throw IllegalArgumentException("Unsupported drawable type.")
        }


    }
    return bitmap
}


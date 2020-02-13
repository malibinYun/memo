package com.malibin.memo.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

fun ByteArray.toBitmap(): Bitmap = BitmapFactory.decodeByteArray(this, 0, this.size)

fun Bitmap.toByteArray(): ByteArray {
    val blob = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 0, blob)
    return blob.toByteArray()
}
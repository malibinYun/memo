package com.malibin.memo.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executors

fun ByteArray.toBitmap(callback: (result: Bitmap) -> Unit) {
    val asyncExecutor = AsyncExecutor()
    asyncExecutor.ioThread.execute {
        val bitmap = BitmapFactory.decodeByteArray(this, 0, this.size)
        asyncExecutor.mainThread.execute { callback(bitmap) }
    }
}

fun Bitmap.toByteArray(callback: (result: ByteArray) -> Unit) {
    val asyncExecutor = AsyncExecutor()
    asyncExecutor.ioThread.execute {
        val blob = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 0, blob)
        asyncExecutor.mainThread.execute { callback(blob.toByteArray()) }
    }
}

fun Bitmap.toByteArray(): ByteArray {
    val blob = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 0, blob)
    return blob.toByteArray()
}

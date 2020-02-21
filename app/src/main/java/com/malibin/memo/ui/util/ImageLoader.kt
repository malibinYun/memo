package com.malibin.memo.ui.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.malibin.memo.db.entity.Image
import com.malibin.memo.util.toByteArray
import org.koin.core.KoinComponent
import org.koin.core.inject

class ImageLoader(private val memoId: String) : KoinComponent, CustomTarget<Bitmap>() {

    private val context: Context by inject()

    private var callback: ((image: Image) -> Unit)? = null

    override fun onLoadCleared(placeholder: Drawable?) {}

    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
        resource.toByteArray {
            val image = Image(ofMemoId = memoId, byteCode = it)
            callback?.invoke(image)
        }
    }

    fun getImage(uri: Uri, callback: (image: Image) -> Unit) {
        this.callback = callback
        Glide.with(context)
            .asBitmap()
            .load(uri)
            .override(1000, 1000)
            .into(this)
    }

    fun getImage(imageUrl: String, callback: (image: Image) -> Unit) {
        this.callback = callback
        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .into(this)
    }
}
package com.malibin.memo.ui.externalimage

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.malibin.memo.R
import com.malibin.memo.databinding.ActivityGetExternalImageBinding
import com.malibin.memo.util.GET_EXTERNAL_IMAGE_RESULT

class GetExternalImageActivity : AppCompatActivity(),
    RequestListener<Drawable> {

    private lateinit var binding: ActivityGetExternalImageBinding

    private var currentInputUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_get_external_image)

        binding.btnLoadImage.setOnClickListener { loadImageFromUrl(getInputUrlText()) }
        binding.btnGetImage.setOnClickListener { onGetImage() }
        binding.btnGetImage.isEnabled = false
    }

    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
    ): Boolean {
        binding.btnGetImage.isEnabled = false
        return false
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        binding.btnGetImage.isEnabled = true
        return false
    }

    private fun onGetImage() {
        intent.putExtra("imageUrl", currentInputUrl)
        setResult(GET_EXTERNAL_IMAGE_RESULT, intent)
        finish()
    }

    private fun loadImageFromUrl(imageUrl: String) {
        currentInputUrl = imageUrl
        Glide.with(this)
            .load(imageUrl)
            .error(R.drawable.cannot_load_image)
            .addListener(this)
            .into(binding.ivImage)
    }

    private fun getInputUrlText(): String {
        return binding.etExternalUrl.text.toString()
    }

    companion object {
        const val REQUEST_CODE = 1008
    }
}

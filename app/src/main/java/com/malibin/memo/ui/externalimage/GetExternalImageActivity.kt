package com.malibin.memo.ui.externalimage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.malibin.memo.R
import kotlinx.android.synthetic.main.activity_get_external_image.*

class GetExternalImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_external_image)
    }

    companion object {
        const val REQUEST_CODE = 1008
    }
}

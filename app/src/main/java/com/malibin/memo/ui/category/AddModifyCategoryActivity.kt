package com.malibin.memo.ui.category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.malibin.memo.R
import com.malibin.memo.databinding.ActivityAddModifyCategoryBinding

class AddModifyCategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityAddModifyCategoryBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_add_modify_category)

        Log.d("Malibin Debug", "${getResources().getDisplayMetrics().density * 160f}")

        val metrics = DisplayMetrics()
        val adf = getWindowManager().getDefaultDisplay().getMetrics(metrics)

        Log.d("Malibin Debug", "width = ${metrics.widthPixels} height = ${metrics.heightPixels}")
    }
}

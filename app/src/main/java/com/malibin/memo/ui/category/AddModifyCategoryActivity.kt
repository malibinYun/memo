package com.malibin.memo.ui.category

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.malibin.memo.R
import com.malibin.memo.databinding.ActivityAddModifyCategoryBinding
import org.koin.android.ext.android.inject

class AddModifyCategoryActivity : AppCompatActivity() {

    private val viewModelFactory: ViewModelProvider.Factory by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel =
            ViewModelProvider(this, viewModelFactory)[AddModifyCategoryViewModel::class.java]

        val binding: ActivityAddModifyCategoryBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_add_modify_category)

        binding.addModifyCategoryVM = viewModel
        binding.lifecycleOwner = this
    }
}

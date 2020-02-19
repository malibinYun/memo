package com.malibin.memo.ui.memo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.malibin.memo.R
import com.malibin.memo.databinding.ActivityMemoEditBinding
import org.koin.android.ext.android.inject

class MemoEditActivity : AppCompatActivity() {

    private val viewModelFactory: ViewModelProvider.Factory by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this, viewModelFactory)[MemoEditViewModel::class.java]

        val binding: ActivityMemoEditBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_memo_edit)

        binding.apply {
            memoEditVM = viewModel
            lifecycleOwner = this@MemoEditActivity
        }
    }
}

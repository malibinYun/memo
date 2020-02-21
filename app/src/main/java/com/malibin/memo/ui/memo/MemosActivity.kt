package com.malibin.memo.ui.memo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.malibin.memo.R
import com.malibin.memo.databinding.ActivityMemosBinding
import org.koin.android.ext.android.inject

class MemosActivity : AppCompatActivity() {

    private val viewModelFactory: ViewModelProvider.Factory by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this, viewModelFactory)[MemosViewModel::class.java]

        val memosAdapter = MemosAdapter(viewModel, this)

        val binding: ActivityMemosBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_memos)
        binding.apply {
            lifecycleOwner = this@MemosActivity
            memosVM = viewModel
            rvMemos.adapter = memosAdapter
        }

        subscribeMemos(viewModel, memosAdapter)
        subscribeToastMessage(viewModel)
    }

    private fun subscribeMemos(viewModel: MemosViewModel, memosAdapter: MemosAdapter) {
        viewModel.items.observe(this, Observer {
            memosAdapter.submitList(it)
        })
    }

    private fun subscribeToastMessage(viewModel: MemosViewModel) {
        viewModel.toastMessage.observe(this, Observer { stringId ->
            Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show()
        })
    }
}

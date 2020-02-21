package com.malibin.memo.ui.memo.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.malibin.memo.databinding.DialogMemoImageBinding
import com.malibin.memo.ui.memo.edit.MemoEditViewModel
import com.malibin.memo.ui.memo.edit.MemoImageAddListener

class MemoImageDialog(
    context: Context,
    private val memoEditViewModel: MemoEditViewModel
) : Dialog(context), MemoImageAddListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DialogMemoImageBinding.inflate(layoutInflater).apply {
            clickListener = this@MemoImageDialog
        }
        setContentView(binding.root)
    }

    override fun getImageFromGallery() {
        memoEditViewModel.deployGallery()
        dismiss()
    }

    override fun getImageFromCamera() {
        memoEditViewModel.deployCamera()
        dismiss()
    }

    override fun getImageFromUrl() {
        dismiss()
    }
}

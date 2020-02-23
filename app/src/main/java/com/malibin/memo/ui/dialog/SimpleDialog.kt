package com.malibin.memo.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.malibin.memo.R
import com.malibin.memo.databinding.DialogSimpleTwoButtonBinding

open class SimpleDialog(context: Context) : AlertDialog(context), SimpleDialogOnClickListener {

    var title: String = ""
    var content: String = ""

    private var cancelClickListener: ((view: View) -> Unit)? = null
    private var okClickListener: ((view: View) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DialogSimpleTwoButtonBinding.inflate(layoutInflater).apply {
            clickListener = this@SimpleDialog
            title.text = this@SimpleDialog.title
            content.text = this@SimpleDialog.content
        }
        setContentView(binding.root)
        setTransparentWindowBackground()
    }

    private fun setTransparentWindowBackground() {
        window?.setBackgroundDrawableResource(R.color.transparent)
    }

    override fun onCancelClick(view: View) {
        cancelClickListener?.invoke(view)
    }

    override fun onOkClick(view: View) {
        okClickListener?.invoke(view)
    }

    fun setSimpleDialogOnClickListener(listener: SimpleDialogOnClickListener) {
        cancelClickListener = listener::onCancelClick
        okClickListener = listener::onOkClick
    }

    fun setCancelClickListener(listener: ((view: View) -> Unit)?) {
        cancelClickListener = listener
    }

    fun setOkClickListener(listener: ((view: View) -> Unit)?) {
        okClickListener = listener
    }
}

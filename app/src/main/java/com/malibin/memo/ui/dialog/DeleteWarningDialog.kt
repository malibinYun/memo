package com.malibin.memo.ui.dialog

import android.content.Context
import android.view.View
import com.malibin.memo.R

class DeleteWarningDialog(context: Context) : SimpleDialog(context) {

    init {
        title = context.getString(R.string.warning)
        content = context.getString(R.string.delete_cause_cascade_contents)
    }

    override fun onCancelClick(view: View) {
        super.onCancelClick(view)
        dismiss()
    }

    override fun onOkClick(view: View) {
        super.onOkClick(view)
        dismiss()
    }
}
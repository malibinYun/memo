package com.malibin.memo.ui.dialog

import android.content.Context
import android.view.View

class DeleteWarningDialog(context: Context) : SimpleDialog(context) {

    init {
        title = "경고"
        content = "삭제하면 연관된 내용이 모두 삭제되며 복구할 수 없습니다. 그래도 삭제하시겠습니까?"
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
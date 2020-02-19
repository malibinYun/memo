package com.malibin.memo.ui.memo.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.malibin.memo.R

class MemoImageDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_memo_image)
    }
}

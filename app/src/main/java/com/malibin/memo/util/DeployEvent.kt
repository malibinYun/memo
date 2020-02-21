package com.malibin.memo.util

class DeployEvent(val deployCode: Int) {

    companion object {
        const val GALLERY = 1
        const val CAMERA = 2
        const val SELECT_CATEGORY_ACT = 3
        const val MEMO_IMAGE_DIALOG = 4
    }
}
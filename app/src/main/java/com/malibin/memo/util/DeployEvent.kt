package com.malibin.memo.util

class DeployEvent(val deployCode: Int) {

    companion object {
        const val GALLERY = 1
        const val CAMERA = 2
        const val SELECT_CATEGORY_ACT = 3
        const val MEMO_IMAGE_DIALOG = 4
        const val FILTER_CATEGORY_ACT = 5
        const val NEW_MEMO_EDIT_ACT = 6
        const val GET_EXTERNAL_IMAGE_ACT = 7
        const val DELETE_WARNING_DIALOG = 8
    }
}
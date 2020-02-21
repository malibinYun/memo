package com.malibin.memo.ui.memo.edit

interface MemoEditNavigator {

    fun onMemoSaved()

    fun onMemoDeleted()

    fun onEditMemoCanceled()

    fun selectCategory()

    fun getExternalImage()

}
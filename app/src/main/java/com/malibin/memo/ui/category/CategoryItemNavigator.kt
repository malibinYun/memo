package com.malibin.memo.ui.category

interface CategoryItemNavigator {

    fun filterMemosWith(categoryId: String)

    fun modifyItem(categoryId: String)
}
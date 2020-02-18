package com.malibin.memo.ui.category

interface CategoriesItemNavigator {

    fun filterMemosWith(categoryId: String)

    fun modifyItem(categoryId: String)
}
package com.malibin.memo.ui.category.select

interface CategorySelectNavigator {

    fun onItemSelected(categoryId: String)

    fun addNewCategory()
}
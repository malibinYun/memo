package com.malibin.memo.db

import com.malibin.memo.db.entity.Category

interface CategoryDataSource {

    fun getCategories(callback: (memo: List<Category>) -> Unit)

    fun getCategory(categoryId: String, callback: (memo: Category) -> Unit)

    fun saveCategory(category: Category)

    fun modifyCategory(category: Category)

    fun deleteCategory(categoryId: String)

}
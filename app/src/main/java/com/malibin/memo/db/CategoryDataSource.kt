package com.malibin.memo.db

import com.malibin.memo.db.entity.Category

interface CategoryDataSource {

    fun getCategories(callback: (categories: List<Category>) -> Unit)

    fun getCategory(categoryId: String, callback: (category: Category) -> Unit)

    fun saveCategory(category: Category)

    fun modifyCategory(category: Category)

    fun deleteCategory(categoryId: String)

}
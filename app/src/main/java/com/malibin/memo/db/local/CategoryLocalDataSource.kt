package com.malibin.memo.db.local

import com.malibin.memo.db.CategoryDataSource
import com.malibin.memo.db.dao.CategoryDao
import com.malibin.memo.db.entity.Category

class CategoryLocalDataSource private constructor(categoryDao: CategoryDao) : CategoryDataSource {

    override fun getCategories(callback: (memo: List<Category>) -> Unit) {

    }

    override fun getCategory(categoryId: String, callback: (memo: Category) -> Unit) {

    }

    override fun saveCategory(category: Category) {

    }

    override fun modifyCategory(category: Category) {

    }

    override fun deleteCategory(categoryId: String) {

    }

    companion object {
        private var INSTANCE: CategoryLocalDataSource? = null

        @JvmStatic
        @Synchronized
        fun getInstance(categoryDao: CategoryDao): CategoryLocalDataSource = INSTANCE
            ?: CategoryLocalDataSource(categoryDao).apply { INSTANCE = this }
    }

}
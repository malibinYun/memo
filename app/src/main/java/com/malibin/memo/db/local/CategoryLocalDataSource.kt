package com.malibin.memo.db.local

import com.malibin.memo.db.CategoryDataSource
import com.malibin.memo.db.dao.CategoryDao
import com.malibin.memo.db.entity.Category
import com.malibin.memo.util.AsyncExecutor

class CategoryLocalDataSource private constructor(
    private val asyncExecutor: AsyncExecutor,
    private val categoryDao: CategoryDao
) : CategoryDataSource {

    override fun getAllCategories(callback: (categories: List<Category>) -> Unit) {
        asyncExecutor.ioThread.execute {
            val categories = categoryDao.getAllCategories()
            asyncExecutor.mainThread.execute { callback(categories) }
        }
    }

    override fun getCategory(categoryId: String, callback: (category: Category?) -> Unit) {
        asyncExecutor.ioThread.execute {
            val category = categoryDao.getCategoryById(categoryId)
            asyncExecutor.mainThread.execute { callback(category) }
        }
    }

    override fun saveCategory(category: Category) {
        asyncExecutor.ioThread.execute { categoryDao.insertCategory(category) }
    }

    override fun modifyCategory(category: Category) {
        asyncExecutor.ioThread.execute { categoryDao.updateCategory(category) }
    }

    override fun deleteCategory(categoryId: String) {
        asyncExecutor.ioThread.execute { categoryDao.deleteCategoryById(categoryId) }
    }

    override fun deleteAllCategories() {
        asyncExecutor.ioThread.execute { categoryDao.deleteAllCategories() }
    }

    companion object {
        private var INSTANCE: CategoryLocalDataSource? = null

        @JvmStatic
        @Synchronized
        fun getInstance(
            asyncExecutor: AsyncExecutor,
            categoryDao: CategoryDao
        ): CategoryLocalDataSource = INSTANCE
            ?: CategoryLocalDataSource(asyncExecutor, categoryDao).apply { INSTANCE = this }
    }

}
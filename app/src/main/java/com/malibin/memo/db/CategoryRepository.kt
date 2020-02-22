package com.malibin.memo.db

import com.malibin.memo.db.entity.Category
import com.malibin.memo.db.source.CategoryDataSource

class CategoryRepository(
    private val categoryLocalDataSource: CategoryDataSource
) : CategoryDataSource {

    // 데이터 캐싱은 시간이 남으면 적용하자.

    override fun getAllCategories(callback: (categories: List<Category>) -> Unit) {
        categoryLocalDataSource.getAllCategories(callback)
    }

    override fun getCategory(categoryId: String, callback: (category: Category?) -> Unit) {
        categoryLocalDataSource.getCategory(categoryId, callback)
    }

    override fun saveCategory(category: Category) {
        categoryLocalDataSource.saveCategory(category)
    }

    override fun modifyCategory(category: Category) {
        categoryLocalDataSource.modifyCategory(category)
    }

    override fun deleteCategory(categoryId: String) {
        categoryLocalDataSource.deleteCategory(categoryId)
    }

    override fun deleteAllCategories() {
        categoryLocalDataSource.deleteAllCategories()
    }
}
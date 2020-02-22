package com.malibin.memo.db

import com.malibin.memo.db.entity.Category
import com.malibin.memo.db.source.CategoryDataSource
import java.util.NoSuchElementException

class CategoryRepository(
    private val categoryLocalDataSource: CategoryDataSource
) : CategoryDataSource {

    private val categoryMapCache = HashMap<String, Category>()

    init {
        getAllCategories {
            refreshCache(it)
        }
    }

    override fun getAllCategories(callback: (categories: List<Category>) -> Unit) {
        if (categoryMapCache.isNotEmpty()) {
            callback(ArrayList(categoryMapCache.values))
            return
        }
        categoryLocalDataSource.getAllCategories {
            refreshCache(it)
            callback(ArrayList(categoryMapCache.values))
        }
    }

    private fun refreshCache(categories: List<Category>) {
        categoryMapCache.clear()
        for (category in categories) {
            categoryMapCache[category.id] = category
        }
    }

    override fun getCategory(categoryId: String, callback: (category: Category?) -> Unit) {
        val cachedCategory = getCategoryById(categoryId)
        if (cachedCategory != null) {
            callback(cachedCategory)
            return
        }
        categoryLocalDataSource.getCategory(categoryId) { fetchedCategory ->
            if (fetchedCategory == null) throw NoSuchElementException("cannot find category")
            cacheCategory(fetchedCategory)
            callback(fetchedCategory)
        }
    }

    private fun getCategoryById(categoryId: String) = categoryMapCache[categoryId]

    override fun saveCategory(category: Category) {
        cacheCategory(category)
        categoryLocalDataSource.saveCategory(category)
    }

    override fun modifyCategory(category: Category) {
        cacheCategory(category)
        categoryLocalDataSource.modifyCategory(category)
    }

    override fun deleteCategory(categoryId: String) {
        categoryMapCache.remove(categoryId)
        categoryLocalDataSource.deleteCategory(categoryId)
    }

    override fun deleteAllCategories() {
        categoryMapCache.clear()
        categoryLocalDataSource.deleteAllCategories()
    }

    private fun cacheCategory(category: Category) {
        categoryMapCache[category.id] = category
    }
}
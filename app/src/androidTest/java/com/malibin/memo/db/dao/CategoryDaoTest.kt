package com.malibin.memo.db.dao

import com.malibin.memo.db.AppDatabase
import com.malibin.memo.db.entity.Category
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class CategoryDaoTest : KoinTest {

    private val database: AppDatabase by inject()
    private lateinit var categoryDao: CategoryDao

    @Before
    fun initCategoryDao() {
        categoryDao = database.categoryDao()
    }

    @After
    fun deleteCreatedCategory() {
        categoryDao.deleteCategoryById(DEFAULT_CATEGORY.id)
    }

    @Test
    fun insertCategory_getAllCategories() {
        categoryDao.insertCategory(DEFAULT_CATEGORY)

        val retrievesCategories = categoryDao.getAllCategories()

        assertEquals(1, retrievesCategories.size)
        assertEquals(DEFAULT_CATEGORY, retrievesCategories[0])
    }

    @Test
    fun insertCategory_getById() {
        categoryDao.insertCategory(DEFAULT_CATEGORY)

        val retrievesCategory = categoryDao.getCategoryById(DEFAULT_CATEGORY.id)

        assertEquals(DEFAULT_CATEGORY, retrievesCategory)
    }

    @Test
    fun insertCategory_replacesOnConflict() {
        categoryDao.insertCategory(DEFAULT_CATEGORY)

        val newCategorySameId = Category(DEFAULT_CATEGORY.id, NEW_NAME, NEW_COLOR)
        categoryDao.insertCategory(newCategorySameId)

        val retrievesCategory = categoryDao.getCategoryById(DEFAULT_CATEGORY.id)

        assertEquals(newCategorySameId, retrievesCategory)
    }

    @Test
    fun updateCategory_getById() {
        categoryDao.insertCategory(DEFAULT_CATEGORY)

        val updatedCategory = Category(DEFAULT_CATEGORY.id, NEW_NAME, NEW_COLOR)
        categoryDao.updateCategory(updatedCategory)

        val retrievesCategory = categoryDao.getCategoryById(DEFAULT_CATEGORY.id)

        assertEquals(updatedCategory, retrievesCategory)
    }

    @Test
    fun deleteCategoryById_getById() {
        categoryDao.insertCategory(DEFAULT_CATEGORY)

        categoryDao.deleteCategoryById(DEFAULT_CATEGORY.id)

        val deletedCategory: Category? = categoryDao.getCategoryById(DEFAULT_CATEGORY.id)

        assertNull(deletedCategory)
    }

    @Test
    fun deleteAllCategories_getAllCategories() {
        categoryDao.insertCategory(DEFAULT_CATEGORY)

        categoryDao.deleteAllCategories()

        val retrievesCategories = categoryDao.getAllCategories()

        assertEquals(0, retrievesCategories.size)
    }

    companion object {
        private const val DEFAULT_NAME = "default category"
        private val DEFAULT_CATEGORY = Category(name = DEFAULT_NAME)
        private val DEFAULT_COLOR = Category.Color.GRAY.name

        private const val NEW_NAME = "new category"
        private val NEW_COLOR = Category.Color.BLUE.name
    }
}
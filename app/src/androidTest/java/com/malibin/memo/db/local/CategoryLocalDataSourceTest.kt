package com.malibin.memo.db.local

import com.malibin.memo.db.entity.Category
import org.junit.After
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.koin.test.KoinTest
import org.koin.test.inject
import java.util.concurrent.CountDownLatch

class CategoryLocalDataSourceTest : KoinTest {

    private val localDataSource: CategoryLocalDataSource by inject()
    private var insertedCategoryId = ""

    lateinit var countDownLatch: CountDownLatch

    @Before
    fun prepareCountDownLatch() {
        countDownLatch = CountDownLatch(1)
    }

    @After
    fun deleteCreatedCategory_afterTest() {
        localDataSource.deleteCategory(insertedCategoryId)
    }

    @Test(timeout = 2000)
    fun saveCategory_retrievesCategory() {
        val newCategory = Category(name = "Test")
        var retrievedCategory: Category? = null

        localDataSource.saveCategory(newCategory)
        insertedCategoryId = newCategory.id

        localDataSource.getCategory(newCategory.id) {
            retrievedCategory = it
            countDownLatch.countDown()
        }
        countDownLatch.await()
        assertEquals(retrievedCategory, newCategory)
    }

    @Test(timeout = 2000)
    fun updateCategory_retrievesCategory() {
        val newCategory = Category(name = "Test")
        val modifiedCategory: Category?
        var retrievedCategory: Category? = null

        localDataSource.saveCategory(newCategory)
        insertedCategoryId = newCategory.id

        modifiedCategory = newCategory.copy()
            .apply { colorCode = Category.Color.BLACK.name }
        localDataSource.modifyCategory(modifiedCategory)

        localDataSource.getCategory(newCategory.id) {
            retrievedCategory = it
            countDownLatch.countDown()
        }
        countDownLatch.await()
        assertEquals(modifiedCategory, retrievedCategory)
    }
}
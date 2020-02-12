package com.malibin.memo.db.dao

import androidx.room.*
import com.malibin.memo.db.entity.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category")
    fun getCategories(): List<Category>

    @Query("SELECT * FROM category WHERE category_id = :categoryId")
    fun getCategoryById(categoryId: String): Category

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: Category)

    @Update
    fun updateCategory(category: Category)

    @Query("DELETE FROM category WHERE category_id = :categoryId")
    fun deleteCategoryById(categoryId: String)

}
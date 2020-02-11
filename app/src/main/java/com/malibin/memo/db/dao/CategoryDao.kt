package com.malibin.memo.db.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.malibin.memo.db.entity.Category

interface CategoryDao {

    @Query("SELECT * FROM category")
    fun getCategories(): List<Category>

    @Query("SELECT * FROM category WHERE category_id = :categoryId")
    fun getCategoryById(categoryId: String): Category?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: Category)

    @Update
    fun updateCategory(category: Category)

    @Query("DELETE FROM category WHERE category_id = :categoryId")
    fun deleteCategoryById(categoryId: String)

}
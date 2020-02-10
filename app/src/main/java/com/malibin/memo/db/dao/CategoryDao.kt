package com.malibin.memo.db.dao

import androidx.room.Query
import com.malibin.memo.db.entity.Category

interface CategoryDao {

    @Query("SELECT * FROM category")
    fun getCategories(): List<Category>

}
package com.malibin.memo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.malibin.memo.db.dao.CategoryDao
import com.malibin.memo.db.dao.MemoDao
import com.malibin.memo.db.entity.Category
import com.malibin.memo.db.entity.Image
import com.malibin.memo.db.entity.Memo

@Database(entities = [Memo::class, Category::class, Image::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun memoDao(): MemoDao

    abstract fun categoryDao(): CategoryDao
}
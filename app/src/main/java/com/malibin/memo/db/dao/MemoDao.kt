package com.malibin.memo.db.dao

import androidx.room.Query
import com.malibin.memo.db.entity.Memo

interface MemoDao {

    @Query("SELECT * FROM memo")
    fun getMemos(): List<Memo>

}
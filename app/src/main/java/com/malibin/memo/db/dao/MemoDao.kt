package com.malibin.memo.db.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.malibin.memo.db.entity.Memo

interface MemoDao {

    @Query("SELECT * FROM memo")
    fun getMemos(): List<Memo>

    @Query("SELECT * FROM memo WHERE memo_id = :memoId")
    fun getMemoById(memoId: String): Memo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMemo(memo: Memo)

    @Update
    fun updateMemo(memo: Memo)

    @Query("DELETE FROM memo WHERE memo_id = :memoId")
    fun deleteMemoById(memoId: String)

}
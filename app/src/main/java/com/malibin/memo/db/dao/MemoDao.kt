package com.malibin.memo.db.dao

import androidx.room.*
import com.malibin.memo.db.entity.Image
import com.malibin.memo.db.entity.Memo

@Dao
abstract class MemoDao {

    @Query("SELECT * FROM memo")
    abstract fun getMemos(): List<Memo>

    @Query("SELECT * FROM memo WHERE memo_id = :memoId")
    abstract fun getMemoById(memoId: String): Memo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMemo(memo: Memo)

    @Update
    abstract fun updateMemo(memo: Memo)

    @Query("DELETE FROM memo WHERE memo_id = :memoId")
    abstract fun deleteMemoById(memoId: String)

    @Query("SELECT * FROM image WHERE memo_id = :memoId")
    abstract fun getImagesByMemoId(memoId: String): List<Image>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertImage(images: List<Image>)

    @Transaction
    open fun saveMemo(memo: Memo) {
        insertMemo(memo)
        insertImage(memo.images)
    }

    @Transaction
    open fun modifyMemo(memo: Memo) {
        updateMemo(memo)
        insertImage(memo.images)
    }

}
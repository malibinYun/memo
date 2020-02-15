package com.malibin.memo.db.dao

import androidx.room.*
import com.malibin.memo.db.entity.Image
import com.malibin.memo.db.entity.Memo

@Dao
abstract class MemoDao {

    @Query("SELECT * FROM memo")
    abstract fun getMemosNoImages(): List<Memo>

    @Query("SELECT * FROM memo WHERE memo_id = :memoId")
    abstract fun getMemoNoImagesById(memoId: String): Memo?

    @Query("SELECT * FROM image WHERE memo_id = :memoId")
    abstract fun getImagesByMemoId(memoId: String): List<Image>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMemoNoImages(memo: Memo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertImages(images: List<Image>)

    @Update
    abstract fun updateMemo(memo: Memo)

    @Query("DELETE FROM memo WHERE memo_id = :memoId")
    abstract fun deleteMemoById(memoId: String)

    @Delete
    abstract fun deleteImages(images: List<Image>)

    @Transaction
    open fun saveMemo(memo: Memo) {
        insertMemoNoImages(memo)
        insertImages(memo.getImages())
    }

    @Transaction
    open fun modifyMemo(memo: Memo, deletedImages: List<Image>) {
        updateMemo(memo)
        insertImages(memo.getImages())
        deleteImages(deletedImages)
    }

}
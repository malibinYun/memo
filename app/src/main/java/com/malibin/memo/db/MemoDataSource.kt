package com.malibin.memo.db

import com.malibin.memo.db.entity.Memo

interface MemoDataSource {

    fun getMemos(callback: (memo: List<Memo>) -> Unit)

    fun getMemo(memoId: String, callback: (memo: Memo) -> Unit)

    fun saveMemo(memo: Memo)

    fun modifyMemo(memo: Memo)

    fun deleteMemo(memoId: String)

}
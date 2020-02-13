package com.malibin.memo.db.source.local

import com.malibin.memo.db.source.MemoDataSource
import com.malibin.memo.db.dao.MemoDao
import com.malibin.memo.db.entity.Memo

class MemoLocalDataSource private constructor(
    private val memoDao: MemoDao
) : MemoDataSource {

    override fun getMemos(callback: (memo: List<Memo>) -> Unit) {

    }

    override fun getMemo(memoId: String, callback: (memo: Memo) -> Unit) {

    }

    override fun saveMemo(memo: Memo) {

    }

    override fun modifyMemo(memo: Memo) {

    }

    override fun deleteMemo(memoId: String) {

    }

    companion object {
        private var INSTANCE: MemoLocalDataSource? = null

        @JvmStatic
        @Synchronized
        fun getInstance(memoDao: MemoDao): MemoLocalDataSource = INSTANCE
            ?: MemoLocalDataSource(memoDao).apply { INSTANCE = this }
    }
}
package com.malibin.memo.db

import com.malibin.memo.db.entity.Memo
import com.malibin.memo.db.source.MemoDataSource

class MemoRepository(
    private val memoLocalDataSource: MemoDataSource
) : MemoDataSource {

    override fun getMemos(callback: (memo: List<Memo>) -> Unit) {
        memoLocalDataSource.getMemos(callback)
    }

    override fun getMemo(memoId: String, callback: (memo: Memo) -> Unit) {
        memoLocalDataSource.getMemo(memoId, callback)
    }

    override fun saveMemo(memo: Memo) {
        memoLocalDataSource.saveMemo(memo)
    }

    override fun modifyMemo(memo: Memo) {
        memoLocalDataSource.modifyMemo(memo)
    }

    override fun deleteMemo(memoId: String) {
        memoLocalDataSource.deleteMemo(memoId)
    }
}
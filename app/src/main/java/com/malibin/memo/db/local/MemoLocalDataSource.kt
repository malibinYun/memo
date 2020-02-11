package com.malibin.memo.db.local

import com.malibin.memo.db.MemoDataSource
import com.malibin.memo.db.dao.MemoDao
import com.malibin.memo.db.entity.Memo
import com.malibin.memo.util.executeAsyncInIoThread
import com.malibin.memo.util.executeAsyncInMainThread

class MemoLocalDataSource private constructor(
    private val memoDao: MemoDao
) : MemoDataSource {

    override fun getMemos(callback: (memo: List<Memo>) -> Unit) {
        executeAsyncInMainThread(memoDao::getMemos, this::handleError, callback)
    }

    override fun getMemo(memoId: String, callback: (memo: Memo) -> Unit) {
        executeAsyncInMainThread({
            memoDao.getMemoById(memoId)
        }, this::handleError) {
            
        }
    }

    override fun saveMemo(memo: Memo) {

    }

    override fun modifyMemo(memo: Memo) {

    }

    override fun deleteMemo(memoId: String) {

    }

    private fun handleError(t: Throwable) {

    }

    companion object {
        private var INSTANCE: MemoLocalDataSource? = null

        @JvmStatic
        @Synchronized
        fun getInstance(memoDao: MemoDao): MemoLocalDataSource = INSTANCE
            ?: MemoLocalDataSource(memoDao).apply { INSTANCE = this }
    }
}
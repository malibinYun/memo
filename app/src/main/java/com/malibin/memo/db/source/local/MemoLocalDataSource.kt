package com.malibin.memo.db.source.local

import com.malibin.memo.db.source.MemoDataSource
import com.malibin.memo.db.dao.MemoDao
import com.malibin.memo.db.entity.Image
import com.malibin.memo.db.entity.Memo
import com.malibin.memo.util.AsyncExecutor

class MemoLocalDataSource private constructor(
    private val asyncExecutor: AsyncExecutor,
    private val memoDao: MemoDao
) : MemoDataSource {

    override fun getMemosNoImages(callback: (memo: List<Memo>) -> Unit) {
        asyncExecutor.ioThread.execute {
            val memos = memoDao.getMemosNoImages()
            asyncExecutor.mainThread.execute { callback(memos) }
        }
    }

    override fun getMemo(memoId: String, callback: (memo: Memo?) -> Unit) {
        asyncExecutor.ioThread.execute {
            val imagesOfMemo = memoDao.getImagesByMemoId(memoId)
            val memo = memoDao.getMemoNoImagesById(memoId)
            memo?.loadImages(imagesOfMemo)
            asyncExecutor.mainThread.execute { callback(memo) }
        }
    }

    override fun getImagesOfMemo(memoId: String, callback: (images: List<Image>) -> Unit) {
        asyncExecutor.ioThread.execute {
            val imagesOfMemo = memoDao.getImagesByMemoId(memoId)
            asyncExecutor.mainThread.execute { callback(imagesOfMemo) }
        }
    }

    override fun saveMemo(memo: Memo) {
        asyncExecutor.ioThread.execute { memoDao.saveMemo(memo) }
    }

    override fun modifyMemo(memo: Memo, deletedImages: List<Image>) {
        asyncExecutor.ioThread.execute { memoDao.modifyMemo(memo, deletedImages) }
    }

    override fun deleteMemoById(memoId: String) {
        asyncExecutor.ioThread.execute { memoDao.deleteMemoById(memoId) }
    }

    companion object {
        private var INSTANCE: MemoLocalDataSource? = null

        @JvmStatic
        @Synchronized
        fun getInstance(
            asyncExecutor: AsyncExecutor,
            memoDao: MemoDao
        ): MemoLocalDataSource = INSTANCE
            ?: MemoLocalDataSource(asyncExecutor, memoDao).apply { INSTANCE = this }
    }
}
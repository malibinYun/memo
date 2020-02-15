package com.malibin.memo.db.source

import com.malibin.memo.db.entity.Image
import com.malibin.memo.db.entity.Memo

interface MemoDataSource {

    fun getMemosNoImages(callback: (memo: List<Memo>) -> Unit)

    fun getMemo(memoId: String, callback: (memo: Memo?) -> Unit)

    fun getImagesOfMemo(memoId: String, callback: (images: List<Image>) -> Unit)

    fun saveMemo(memo: Memo)

    fun modifyMemo(memo: Memo, deletedImages: List<Image>)

    fun deleteMemoById(memoId: String)

}
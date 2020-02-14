package com.malibin.memo.db

import com.malibin.memo.db.entity.Image
import com.malibin.memo.db.entity.Memo
import com.malibin.memo.db.source.MemoDataSource

class MemoRepository(
    private val memoLocalDataSource: MemoDataSource
) {

}
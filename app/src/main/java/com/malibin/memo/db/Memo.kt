package com.malibin.memo.db

import java.util.*

data class Memo(
    val id: String = UUID.randomUUID().toString(),
    var title: String = "",
    val createdDate: Long = System.currentTimeMillis(),
    var categoryId: String = "",
    var isImportant: Boolean = false,
    var content: String = ""
    // var images:
)
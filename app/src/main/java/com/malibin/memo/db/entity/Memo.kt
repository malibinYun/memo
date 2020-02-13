package com.malibin.memo.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Memo(

    @PrimaryKey
    @ColumnInfo(name = "memo_id")
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "memo_title")
    var title: String = "",

    @ColumnInfo(name = "memo_created_date")
    val createdDate: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "memo_category_id")
    var categoryId: String = "",

    @ColumnInfo(name = "memo_content")
    var content: String = ""

) {
    @ColumnInfo(name = "memo_is_important")
    var isImportant: Boolean = false

    @ColumnInfo(name = "memo_thumbnail")
    var thumbnailByteCode: ByteArray? = null

    @ColumnInfo(name = "memo_image_count")
    var imageCount: Int = 0

    @Ignore
    val images: MutableList<Image> = ArrayList()

    fun loadImages(images: List<Image>) {
        this.images.addAll(images)
    }

}
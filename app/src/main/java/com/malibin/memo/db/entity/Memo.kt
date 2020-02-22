package com.malibin.memo.db.entity

import androidx.annotation.VisibleForTesting
import androidx.room.*
import java.util.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["category_id"],
        childColumns = ["memo_category_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
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

    @ColumnInfo(name = "memo_thumbnail", typeAffinity = ColumnInfo.BLOB)
    var thumbnailByteCode: ByteArray? = null

    @ColumnInfo(name = "memo_image_count")
    var imageCount: Int = 0

    @Ignore
    private val _images: MutableList<Image> = ArrayList()

    fun getImages(): List<Image> {
        return _images
    }

    fun loadImages(images: List<Image>) {
        _images.addAll(images)
    }

    fun addImage(image: Image) {
        if (_images.isEmpty()) thumbnailByteCode = image.byteCode
        _images.add(image)
        imageCount++
    }

    fun addImages(images: List<Image>) {
        if (images.isEmpty()) return
        if (this._images.isEmpty()) thumbnailByteCode = images[0].byteCode
        _images.addAll(images)
        imageCount += images.size
    }

    fun removeImageAt(index: Int): Image {
        val removedImage = _images.removeAt(index)
        thumbnailByteCode = if (_images.size < 1) null else _images[0].byteCode
        imageCount--
        return removedImage
    }

    @VisibleForTesting
    fun clearOnlyImageMutableList() {
        _images.clear()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Memo

        if (id != other.id) return false
        if (title != other.title) return false
        if (createdDate != other.createdDate) return false
        if (categoryId != other.categoryId) return false
        if (content != other.content) return false
        if (isImportant != other.isImportant) return false
        if (thumbnailByteCode != null) {
            if (other.thumbnailByteCode == null) return false
            if (!thumbnailByteCode!!.contentEquals(other.thumbnailByteCode!!)) return false
        } else if (other.thumbnailByteCode != null) return false
        if (imageCount != other.imageCount) return false
        if (_images != other._images) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + createdDate.hashCode()
        result = 31 * result + categoryId.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + isImportant.hashCode()
        result = 31 * result + (thumbnailByteCode?.contentHashCode() ?: 0)
        result = 31 * result + imageCount
        result = 31 * result + _images.hashCode()
        return result
    }
}
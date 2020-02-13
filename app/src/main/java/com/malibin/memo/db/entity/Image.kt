package com.malibin.memo.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = Memo::class,
        parentColumns = ["memo_id"],
        childColumns = ["memo_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Image(

    @PrimaryKey
    @ColumnInfo(name = "image_id")
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "memo_id")
    val ofMemoId: String,

    @ColumnInfo(name = "byte_code", typeAffinity = ColumnInfo.BLOB)
    var byteCode: ByteArray

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Image

        if (id != other.id) return false
        if (ofMemoId != other.ofMemoId) return false
        if (!byteCode.contentEquals(other.byteCode)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + ofMemoId.hashCode()
        result = 31 * result + byteCode.contentHashCode()
        return result
    }
}
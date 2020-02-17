package com.malibin.memo.db.entity

import androidx.annotation.ColorRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.malibin.memo.R
import java.util.*

@Entity
data class Category(

    @PrimaryKey
    @ColumnInfo(name = "category_id")
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "category_name")
    var name: String,

    @ColumnInfo(name = "category_color_code")
    var colorCode: String = Color.GRAY.name

) {
    enum class Color(@ColorRes val resId: Int) {
        RED(R.color.categoryRed),
        ORANGE(R.color.categoryOrange),
        YELLOW(R.color.categoryYellow),
        GREEN(R.color.categoryGreen),
        BLUE(R.color.categoryBlue),
        NAVY(R.color.categoryNavy),
        PURPLE(R.color.categoryPurple),
        PINK(R.color.categoryPink),
        GRAY(R.color.categoryGray),
        BLACK(R.color.categoryBlack);
    }
}
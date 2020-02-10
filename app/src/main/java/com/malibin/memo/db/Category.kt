package com.malibin.memo.db

import androidx.annotation.ColorRes
import com.malibin.memo.R
import java.util.*

data class Category(
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var colorCode: String = Color.GRAY.name
) {
    enum class Color(@ColorRes val resId: Int) {
        RED(R.color.categoryRed),
        ORANGE(R.color.categoryOrange),
        YELLOW(R.color.categoryYellow),
        GREEN(R.color.categoryGreen),
        BLUE(R.color.categoryBlue),
        INDIGO(R.color.categoryIndigo),
        PURPLE(R.color.categoryPurple),
        PINK(R.color.categoryPink),
        GRAY(R.color.categoryGray),
        BLACK(R.color.categoryBlack);
    }
}
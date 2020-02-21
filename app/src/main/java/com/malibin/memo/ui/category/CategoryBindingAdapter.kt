package com.malibin.memo.ui.category

import android.os.Build
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.malibin.memo.db.entity.Category

@BindingAdapter("bind_category_color")
fun bindCategoryColor(view: ImageView, colorCode: String?) {
    val color = Category.Color.valueOf(colorCode ?: return)
    val context = view.context
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        view.backgroundTintList = context.resources.getColorStateList(color.resId)
        return
    }
    view.backgroundTintList = context.resources.getColorStateList(color.resId, context.theme)
}
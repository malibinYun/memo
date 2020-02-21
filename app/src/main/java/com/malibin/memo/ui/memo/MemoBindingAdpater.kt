package com.malibin.memo.ui.memo

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.malibin.memo.R
import com.malibin.memo.db.entity.Category
import com.malibin.memo.util.toBitmap
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("bind_image")
fun bindImage(imageView: ImageView, byteArray: ByteArray?) {
    if (byteArray == null) imageView.visibility = View.GONE
    byteArray?.toBitmap {
        Glide.with(imageView).load(it).into(imageView)
    }
}

@BindingAdapter("bind_memo_date")
fun bindMemoDate(textView: TextView, date: Long) {
    val mDate = Date(date)
    val simpleDateFormat = SimpleDateFormat("MM월 dd일", Locale.KOREA)
    val dateString = simpleDateFormat.format(mDate)
    textView.text = dateString
}

@BindingAdapter("bind_category_text_color")
fun bindCategoryTextColor(textView: TextView, colorCode: String) {
    val colorRes = Category.Color.valueOf(colorCode).resId
    val color = textView.context.getColor(colorRes)
    textView.setTextColor(color)
}

@BindingAdapter("bind_background_color")
fun bindBackgroundColor(view: View, colorCode: String?) {
    val context = view.context
    if (colorCode.isNullOrEmpty()) {
        val whiteColor = context.getColor(android.R.color.white)
        view.setBackgroundColor(whiteColor)
        return
    }
    val colorRes = Category.Color.valueOf(colorCode).resId
    val categoryColor = context.getColor(colorRes)
    view.setBackgroundColor(categoryColor)
}

@BindingAdapter("bind_selected")
fun bindViewSelected(view: View, isSelected: Boolean) {
    view.isSelected = isSelected
}

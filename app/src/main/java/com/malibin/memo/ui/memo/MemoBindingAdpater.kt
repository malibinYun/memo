package com.malibin.memo.ui.memo

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
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
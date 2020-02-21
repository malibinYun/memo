package com.malibin.memo.ui.memo

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.malibin.memo.util.toBitmap
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("bind_image")
fun bindImage(imageView: ImageView, byteArray: ByteArray) {
    byteArray.toBitmap {
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
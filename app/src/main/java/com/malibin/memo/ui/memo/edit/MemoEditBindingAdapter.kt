package com.malibin.memo.ui.memo.edit

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.MutableLiveData
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("bind_selected")
fun bindViewSelected(view: View, isSelected: MutableLiveData<Boolean>) {
    view.isSelected = isSelected.value ?: false
}

@InverseBindingAdapter(attribute = "bind_selected", event = "bind_selectedAttrChanged")
fun getViewSelected(view: View) = view.isSelected

@BindingAdapter("bind_selectedAttrChanged")
fun setAttrChangedListener(view: View, listener: InverseBindingListener) {
    view.setOnClickListener {
        view.isSelected = !view.isSelected
        listener.onChange()
    }
}

@BindingAdapter("bind_date")
fun bindTextDate(view: TextView, date: Long) {
    val mDate = Date(date)
    val simpleDateFormat = SimpleDateFormat("yyyy. MM. dd", Locale.KOREA)
    val dateString = simpleDateFormat.format(mDate)
    view.text = dateString
}
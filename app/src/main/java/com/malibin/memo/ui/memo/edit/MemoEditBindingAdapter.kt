package com.malibin.memo.ui.memo.edit

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager
import com.malibin.memo.ui.util.addOnPageSelectedListener
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

@BindingAdapter("bind_page_position")
fun bindViewPagerPosition(viewPager: ViewPager, position: MutableLiveData<Int>) {
    viewPager.currentItem = -1 + (position.value ?: throw RuntimeException("position is null"))
}

@InverseBindingAdapter(attribute = "bind_page_position", event = "bind_pageAttrPosition")
fun getViewPagerPosition(viewPager: ViewPager) = viewPager.currentItem + 1

@BindingAdapter("bind_pageAttrPosition")
fun setPagerPositionChangedListener(viewPager: ViewPager, listener: InverseBindingListener) {
    viewPager.addOnPageSelectedListener {
        viewPager.currentItem = it
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
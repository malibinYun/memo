package com.malibin.memo.ui.util

import android.content.Context
import android.os.Build
import androidx.viewpager.widget.ViewPager

fun ViewPager.addOnPageSelectedListener(listener: (position: Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            listener.invoke(position)
        }
    })
}

fun Context.getColorMatchVersion(colorRes: Int): Int {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        resources.getColor(colorRes)
    } else {
        this.getColor(colorRes)
    }
}

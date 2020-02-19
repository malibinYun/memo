package com.malibin.memo.ui.memo.edit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.getSystemService
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.malibin.memo.databinding.WindowAddImageBinding
import com.malibin.memo.databinding.WindowImageBinding
import com.malibin.memo.db.entity.Image
import com.malibin.memo.ui.memo.dialog.MemoImageDialog
import com.malibin.memo.util.toBitmap

class MemoImagePagerAdapter(private val context: Context) : PagerAdapter() {

    private val imageList = ArrayList<Image>()

    private var layoutInflater: LayoutInflater =
        context.getSystemService() ?: throw RuntimeException("layout inflater is null")

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as ConstraintLayout
    }

    override fun getCount(): Int = imageList.size + ADD_IMAGE_VIEW_COUNT

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        if (position == getLastPosition()) {
            return createAddImageView(container)
        }
        return createItemView(container, position)
    }

    private fun createAddImageView(container: ViewGroup): View {
        val view = WindowAddImageBinding.inflate(layoutInflater)
        view.setClickListener { MemoImageDialog(context).show() }
        container.addView(view.root)
        return view.root
    }

    private fun createItemView(container: ViewGroup, position: Int): View {
        val itemView = WindowImageBinding.inflate(layoutInflater)
        val image = imageList[position]
        Glide.with(context)
            .load(image.byteCode.toBitmap())
            .into(itemView.image)
        container.addView(itemView.root)
        return itemView.root
    }

    fun submitList(imageList: List<Image>) {
        this.imageList.clear()
        this.imageList.addAll(imageList)
        notifyDataSetChanged()
    }

    private fun getLastPosition(): Int {
        return imageList.size
    }

    companion object {
        const val ADD_IMAGE_VIEW_COUNT = 1
    }
}
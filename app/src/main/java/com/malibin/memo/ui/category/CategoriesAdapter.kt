package com.malibin.memo.ui.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.malibin.memo.R
import com.malibin.memo.databinding.ItemCategoryBinding
import com.malibin.memo.db.entity.Category

class CategoriesAdapter : ListAdapter<Category, CategoriesAdapter.ViewHolder>(DiffCallBack()) {

    var categoriesViewModel: CategoriesViewModel? = null
    var lifeCycleOwner: LifecycleOwner? = null

    private var itemClickListener: ((categoryId: String) -> Unit)? = null
    private var itemDeleteClickListener: ((categoryId: String) -> Unit)? = null

    private lateinit var showItems: MutableList<Category>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category)
    }

    override fun submitList(list: MutableList<Category>?) {
        super.submitList(list)
        showItems = list ?: mutableListOf()
    }

    fun setItemClickListener(listener: (categoryId: String) -> Unit) {
        itemClickListener = listener
    }

    fun setItemDeleteClickListener(listener: (categoryId: String) -> Unit) {
        itemDeleteClickListener = listener
    }

    private fun createItemClickListener(categoryId: String) = View.OnClickListener {
        itemClickListener?.invoke(categoryId)
    }

    private fun createItemDeleteClickListener(categoryId: String) = View.OnClickListener {
        if (categoryId == "unique") {
            Toast.makeText(it.context, R.string.cannot_delete_basic_category, Toast.LENGTH_SHORT)
                .show()
            return@OnClickListener
        }
        itemDeleteClickListener?.invoke(categoryId)
        val position = showItems.indexOfFirst { it.id == categoryId }
        showItems.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ViewHolder(
        private val binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.apply {
                this.category = category
                this.categoriesViewModel = this@CategoriesAdapter.categoriesViewModel
                this.lifecycleOwner = this@CategoriesAdapter.lifeCycleOwner
                this.itemClickListener = createItemClickListener(category.id)
                this.itemDeleteClickListener = createItemDeleteClickListener(category.id)
            }
        }
    }

    private class DiffCallBack : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }
}
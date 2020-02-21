package com.malibin.memo.ui.memo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.malibin.memo.databinding.ItemMemoBinding
import com.malibin.memo.db.entity.Memo

class MemosAdapter(
    private val memosViewModel: MemosViewModel,
    private val lifecycleOwner: LifecycleOwner
) : ListAdapter<Memo, MemosAdapter.ViewHolder>(DiffCallBack()) {

    private var itemClickListener: ((memoId: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMemoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val memo = getItem(position)
        holder.bind(memo)
    }

    fun setItemClickListener(listener: (memoId: String) -> Unit) {
        itemClickListener = listener
    }

    private fun createItemClickListener(memoId: String) = View.OnClickListener {
        itemClickListener?.invoke(memoId)
    }

    inner class ViewHolder(
        private val binding: ItemMemoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(memo: Memo) {
            binding.memo = memo
            binding.memosVM = memosViewModel
            binding.lifecycleOwner = lifecycleOwner
            binding.itemClickListener = createItemClickListener(memo.id)
        }
    }

    private class DiffCallBack : DiffUtil.ItemCallback<Memo>() {
        override fun areItemsTheSame(oldItem: Memo, newItem: Memo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Memo, newItem: Memo): Boolean {
            return oldItem == newItem
        }
    }
}
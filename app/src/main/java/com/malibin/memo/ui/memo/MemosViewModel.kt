package com.malibin.memo.ui.memo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.malibin.memo.R
import com.malibin.memo.db.CategoryRepository
import com.malibin.memo.db.MemoRepository
import com.malibin.memo.db.entity.Category
import com.malibin.memo.db.entity.Memo
import com.malibin.memo.util.BaseViewModel
import java.lang.RuntimeException

class MemosViewModel(
    private val memoRepository: MemoRepository,
    private val categoryRepository: CategoryRepository
) : BaseViewModel() {

    val isDeleteMode = MutableLiveData<Boolean>().apply { value = false }

    private val _items = MutableLiveData<List<Memo>>().apply { value = emptyList() }
    val items: LiveData<List<Memo>>
        get() = _items

    private val itemIdsToDelete = mutableListOf<String>()

    val categoryMap = HashMap<String, Category>()

    init {
        loadCategories()
        loadMemos()
    }

    private fun loadMemos() {
        _isLoading.value = true
        memoRepository.getMemosNoImages {
            Log.d("MalibinD", it.toString())
            _items.value = it
            _isLoading.value = false
        }
    }

    private fun loadCategories() {
        categoryRepository.getAllCategories {
            Log.d("MalibinD", it.toString())

            for (category in it) {
                categoryMap[category.id] = category
            }
        }
    }

    fun activateDeleteMode() {
        isDeleteMode.value = true
    }

    fun addMemoIdToDelete(itemId: String) {
        itemIdsToDelete.add(itemId)
    }

    fun cancelDeleteMemos() {
        itemIdsToDelete.clear()
        _toastMessage.value = R.string.delete_memo_canceled
        isDeleteMode.value = false
    }

    fun finishDeleteMemos() {
        if (isDeleteMode.value == false) {
            throw RuntimeException("finishDeleteMemos cannot call isDeleteMode is false")
        }
        if (itemIdsToDelete.isEmpty()) {
            isDeleteMode.value = false
            return
        }
        _items.value = createUpdatedMemos()
        itemIdsToDelete.clear()
        _toastMessage.value = R.string.delete_memo_finished
        isDeleteMode.value = false
    }

    private fun createUpdatedMemos(): List<Memo> {
        val updatedMemos = ArrayList<Memo>()
        updatedMemos.addAll(_items.value ?: emptyList())
        for (itemId in itemIdsToDelete) {
            updatedMemos.removeIf { it.id == itemId }
            memoRepository.deleteMemoById(itemId)
        }
        return ArrayList(updatedMemos)
    }

}

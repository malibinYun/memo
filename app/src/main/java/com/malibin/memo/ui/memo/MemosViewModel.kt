package com.malibin.memo.ui.memo

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.malibin.memo.R
import com.malibin.memo.db.CategoryRepository
import com.malibin.memo.db.MemoRepository
import com.malibin.memo.db.entity.Category
import com.malibin.memo.db.entity.Memo
import com.malibin.memo.ui.category.CategoriesActivity
import com.malibin.memo.ui.memo.edit.MemoEditActivity
import com.malibin.memo.util.*
import java.lang.RuntimeException

class MemosViewModel(
    private val memoRepository: MemoRepository,
    private val categoryRepository: CategoryRepository
) : BaseViewModel() {

    val isDeleteMode = MutableLiveData<Boolean>().apply { value = false }

    private val _items = MutableLiveData<List<Memo>>().apply { value = emptyList() }
    val items: LiveData<List<Memo>>
        get() = _items

    private val _filteredCategory = MutableLiveData<Category>()
    val filteredCategory: LiveData<Category>
        get() = _filteredCategory

    private val _isFiltered = MutableLiveData<Boolean>().apply { value = false }
    val isFiltered: LiveData<Boolean>
        get() = _isFiltered

    private val _deployEvent = MutableLiveData<DeployEvent>()
    val deployEvent: LiveData<DeployEvent>
        get() = _deployEvent

    private val itemIdsToDelete = mutableListOf<String>()

    val categoryMap = HashMap<String, Category>()

    init {
        loadCategories()
        loadMemos(ALL_ID)
    }

    private fun loadMemos(categoryId: String) {
        _isLoading.value = true
        memoRepository.getMemosNoImages {
            filterMemos(categoryId, it)
            _isLoading.value = false
        }
    }

    private fun loadCategories() {
        categoryRepository.getAllCategories {
            for (category in it) {
                categoryMap[category.id] = category
            }
        }
    }

    private fun filterMemos(categoryId: String, memos: List<Memo>) {
        _isFiltered.value = false
        if (categoryId == ALL_ID) {
            _items.value = memos
            _filteredCategory.value = ALL_CATEGORY
            return
        }
        val filteredMemos = ArrayList(memos)
        if (categoryId == IMPORTANT_ID) {
            _items.value = filteredMemos.filter { it.isImportant }
            _filteredCategory.value = IMPORTANT_CATEGORY
            return
        }
        _items.value = filteredMemos.filter { it.categoryId == categoryId }
        _filteredCategory.value = categoryMap[categoryId]
        _isFiltered.value = true
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == CategoriesActivity.REQUEST_CODE) {
            if (resultCode == MEMO_IMPORTANT_FILTER_RESULT) {
                loadMemos(IMPORTANT_ID)
            }
            if (resultCode == MEMO_ALL_FILTER_RESULT) {
                loadMemos(ALL_ID)
            }
            if (resultCode == MEMO_CATEGORY_FILTER_RESULT) {
                val categoryId = intent?.getStringExtra("categoryId") ?: return
                loadMemos(categoryId)
            }
        }
        if (requestCode == MemoEditActivity.REQUEST_CODE) {
            if (resultCode == MEMO_SAVED || resultCode == MEMO_DELETED) {
                val currentCategory = _filteredCategory.value ?: return
                refreshMemoAndCategories(currentCategory.id)
                _toastMessage.value = R.string.memo_saved
            }
        }
    }

    private fun refreshMemoAndCategories(categoryId: String) {
        loadCategories()
        loadMemos(categoryId)
    }

    fun deployFilterCategory() {
        _deployEvent.value = DeployEvent(DeployEvent.FILTER_CATEGORY_ACT)
    }

    fun deployMemoEditCategory() {
        _deployEvent.value = DeployEvent(DeployEvent.NEW_MEMO_EDIT_ACT)
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

    companion object {
        private const val IMPORTANT_ID = "important"
        private const val ALL_ID = "all"
        private val IMPORTANT_CATEGORY = Category(IMPORTANT_ID, "중요 메모", "")
        private val ALL_CATEGORY = Category(ALL_ID, "모든 메모", "")
    }
}

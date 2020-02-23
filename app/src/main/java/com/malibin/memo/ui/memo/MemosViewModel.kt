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

    private val _categoryMap = MutableLiveData<Map<String, Category>>()
    val categoryMap: LiveData<Map<String, Category>>
        get() = _categoryMap

    private val itemIdsToDelete = mutableListOf<String>()

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
            _categoryMap.value = createCategoryMap(it)
        }
    }

    private fun createCategoryMap(categories: List<Category>): Map<String, Category> {
        val result = HashMap<String, Category>()
        for (category in categories) {
            result[category.id] = category
        }
        return result
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
        categoryRepository.getCategory(categoryId) {}
        _filteredCategory.value = _categoryMap.value?.get(categoryId) ?: throw RuntimeException()
        _isFiltered.value = true
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        loadCategories()
        detectCurrentCategoryChanged()
        if (requestCode == CategoriesActivity.REQUEST_CODE) {
            if (resultCode == MEMO_IMPORTANT_FILTER_RESULT) {
                loadMemos(IMPORTANT_ID)
                return
            }
            if (resultCode == MEMO_ALL_FILTER_RESULT) {
                loadMemos(ALL_ID)
                return
            }
            if (resultCode == MEMO_CATEGORY_FILTER_RESULT) {
                val categoryId = intent?.getStringExtra("categoryId") ?: return
                loadMemos(categoryId)
                return
            }
        }
        if (requestCode == MemoEditActivity.REQUEST_CODE) {
            if (resultCode == MEMO_SAVED) _toastMessage.value = R.string.memo_saved
            if (resultCode == MEMO_DELETED) _toastMessage.value = R.string.memo_deleted
            if (resultCode == MEMO_EDIT_CANCELED) _toastMessage.value = R.string.edit_memo_canceled
            refreshAll()
        }
    }

    private fun refreshAll() {
        val currentCategory = _filteredCategory.value ?: return
        loadCategories()
        loadMemos(currentCategory.id)
    }

    private fun detectCurrentCategoryChanged() {
        val currentCategory = _filteredCategory.value ?: return
        if (currentCategory == ALL_CATEGORY || currentCategory == IMPORTANT_CATEGORY) {
            return
        }
        categoryRepository.getCategory(currentCategory.id) {
            if (it == null) {
                loadMemos(ALL_ID)
                return@getCategory
            }
            if (it != currentCategory) _filteredCategory.value = it
        }
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
        _toastMessage.value = R.string.memo_deleted
        isDeleteMode.value = false
    }

    private fun createUpdatedMemos(): List<Memo> {
        val updatedMemos = ArrayList<Memo>()
        updatedMemos.addAll(_items.value ?: emptyList())
        for (itemId in itemIdsToDelete) {
            val itemToDelete = updatedMemos.find { it.id == itemId }
            updatedMemos.remove(itemToDelete)
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

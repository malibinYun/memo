package com.malibin.memo.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.malibin.memo.R
import com.malibin.memo.db.CategoryRepository
import com.malibin.memo.db.entity.Category
import com.malibin.memo.util.BaseViewModel
import com.malibin.memo.util.CATEGORY_SAVE_RESULT_OK
import java.lang.RuntimeException

class CategoriesViewModel(
    private val categoryRepository: CategoryRepository
) : BaseViewModel() {

    val isEditMode = MutableLiveData<Boolean>().apply { value = false }

    private val itemIdsToDelete = mutableListOf<String>()

    private val _items = MutableLiveData<List<Category>>().apply { value = emptyList() }
    val items: LiveData<List<Category>>
        get() = _items

    init {
        loadCategories()
    }

    private fun loadCategories() {
        _isLoading.value = true
        categoryRepository.getAllCategories {
            _items.value = it
            _isLoading.value = false
        }
    }

    fun handleActivityResult(resultCode: Int) {
        if (resultCode == CATEGORY_SAVE_RESULT_OK) {
            loadCategories()
            return
        }
    }

    fun activateEditMode() {
        isEditMode.value = true
    }

    fun addCategoryToDelete(itemId: String) {
        itemIdsToDelete.add(itemId)
    }

    fun cancelEditCategories() {
        itemIdsToDelete.clear()
        _toastMessage.value = R.string.edit_category_canceled
        isEditMode.value = false
        val feedback = items.value
        _items.value = feedback
    }

    fun finishEditCategories() {
        if (isEditMode.value == false) {
            throw RuntimeException("finishEditCategories cannot call isEditMode is false")
        }
        if (itemIdsToDelete.isEmpty()) {
            isEditMode.value = false
            return
        }
        _items.value = createUpdatedCategories()
        itemIdsToDelete.clear()
        _toastMessage.value = R.string.edit_category_is_finished
        isEditMode.value = false
    }

    private fun createUpdatedCategories(): List<Category> {
        val updatedCategories = ArrayList<Category>()
        updatedCategories.addAll(_items.value ?: emptyList())
        for (itemId in itemIdsToDelete) {
            updatedCategories.removeIf { it.id == itemId }
            categoryRepository.deleteCategory(itemId)
        }
        return ArrayList(updatedCategories)
    }
}
package com.malibin.memo.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.malibin.memo.R
import com.malibin.memo.db.CategoryRepository
import com.malibin.memo.db.entity.Category
import com.malibin.memo.util.BaseViewModel
import java.lang.RuntimeException

class CategoryViewModel(
    private val categoryRepository: CategoryRepository
) : BaseViewModel() {

    val isEditMode = MutableLiveData<Boolean>().apply { value = false }

    private val itemsToDelete = mutableListOf<Category>()

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

    fun activateEditMode() {
        isEditMode.value = true
    }

    fun addCategoryToDelete(item: Category) {
        itemsToDelete.add(item)
    }

    fun cancelEditCategories() {
        itemsToDelete.clear()
        _toastMessage.value = R.string.edit_category_canceled
        isEditMode.value = false
    }

    fun finishEditCategories() {
        if (isEditMode.value == false) {
            throw RuntimeException("finishEditCategories cannot call isEditMode is false")
        }
        if (itemsToDelete.isEmpty()) {
            isEditMode.value = false
            return
        }
        for (item in itemsToDelete) {
            categoryRepository.deleteCategory(item.id)
        }
        _toastMessage.value = R.string.edit_category_is_finished
        isEditMode.value = false
        itemsToDelete.clear()
    }

}
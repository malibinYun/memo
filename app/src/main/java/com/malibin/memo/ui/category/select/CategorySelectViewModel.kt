package com.malibin.memo.ui.category.select

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.malibin.memo.db.CategoryRepository
import com.malibin.memo.db.entity.Category
import com.malibin.memo.util.BaseViewModel
import com.malibin.memo.util.CATEGORY_SAVE_RESULT_OK

class CategorySelectViewModel(
    private val categoryRepository: CategoryRepository
) : BaseViewModel() {

    private val _items = MutableLiveData<List<Category>>()
    val items: LiveData<List<Category>>
        get() = _items

    private val _newItemEvent = MutableLiveData<Boolean>()
    val newItemEvent: LiveData<Boolean>
        get() = _newItemEvent

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

    fun addNewCategory() {
        _newItemEvent.value = true
    }

    fun handleActivityResult(resultCode: Int) {
        if (resultCode == CATEGORY_SAVE_RESULT_OK) {
            loadCategories()
            return
        }
    }
}

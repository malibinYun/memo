package com.malibin.memo.ui.category.addmodify

import androidx.lifecycle.MutableLiveData
import com.malibin.memo.R
import com.malibin.memo.db.CategoryRepository
import com.malibin.memo.db.entity.Category
import com.malibin.memo.util.BaseViewModel

class AddModifyCategoryViewModel(
    private val categoryRepository: CategoryRepository
) : BaseViewModel() {

    val name = MutableLiveData<String>()

    val selectedColor = MutableLiveData<Category.Color>().apply { value = Category.Color.GRAY }

    private var categoryId: String? = null

    private var isNewCategory = true

    fun start(categoryId: String?) {
        _isLoading.value = true
        this.categoryId = categoryId

        if (categoryId == null) {
            isNewCategory = true
            _isLoading.value = false
            return
        }
        isNewCategory = false
        loadCategory(categoryId)
    }

    private fun loadCategory(categoryId: String) {
        categoryRepository.getCategory(categoryId) {
            val category = it ?: throw RuntimeException("category exist but cannot loaded")
            name.value = category.name
            selectedColor.value = Category.Color.valueOf(category.colorCode)
            _isLoading.value = false
        }
    }

    fun updateSelectedColor(newColor: Category.Color) {
        selectedColor.value = newColor
    }

    fun saveCategory() {
        val currentName = name.value
        val currentColor = selectedColor.value?.name ?: Category.Color.GRAY.name

        if (currentName.isNullOrEmpty()) {
            _toastMessage.value = R.string.input_category
            return
        }
        if (isNewCategory) {
            addCategory(currentName, currentColor)
            return
        }
        modifyCategory(currentName, currentColor)
    }

    private fun addCategory(currentName: String, currentColor: String) {
        val newCategory = Category(name = currentName, colorCode = currentColor)
        categoryRepository.saveCategory(newCategory)
        _isSuccess.value = true
        _toastMessage.value = R.string.category_added
    }

    private fun modifyCategory(currentName: String, currentColor: String) {
        val currentCategoryId =
            categoryId ?: throw RuntimeException("update called but category does not loaded")
        val updatedCategory = Category(currentCategoryId, currentName, currentColor)
        categoryRepository.modifyCategory(updatedCategory)
        _isSuccess.value = true
        _toastMessage.value = R.string.category_modified
    }

}
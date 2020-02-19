package com.malibin.memo.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malibin.memo.db.CategoryRepository
import com.malibin.memo.db.MemoRepository
import com.malibin.memo.ui.category.CategoriesViewModel
import com.malibin.memo.ui.category.addmodify.AddModifyCategoryViewModel
import com.malibin.memo.ui.category.select.CategorySelectViewModel
import com.malibin.memo.ui.memo.edit.MemoEditViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(
    private val memoRepository: MemoRepository,
    private val categoryRepository: CategoryRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {

            CategoriesViewModel::class.java,
            CategorySelectViewModel::class.java,
            AddModifyCategoryViewModel::class.java ->
                modelClass.getConstructor(CATEGORY_REPO).newInstance(categoryRepository)

            MemoEditViewModel::class.java ->
                modelClass.getConstructor(MEMO_REPO, CATEGORY_REPO)
                    .newInstance(memoRepository, categoryRepository)

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        } as T
    }

    companion object {
        private val MEMO_REPO = MemoRepository::class.java
        private val CATEGORY_REPO = CategoryRepository::class.java
    }
}
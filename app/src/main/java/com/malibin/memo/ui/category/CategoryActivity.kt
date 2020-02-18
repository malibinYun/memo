package com.malibin.memo.ui.category

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.malibin.memo.R
import com.malibin.memo.databinding.ActivityCategoryBinding
import com.malibin.memo.ui.category.addmodify.AddModifyCategoryActivity
import com.malibin.memo.util.MEMO_ALL_FILTER_RESULT
import com.malibin.memo.util.MEMO_CATEGORY_FILTER_RESULT
import com.malibin.memo.util.MEMO_IMPORTANT_FILTER_RESULT
import org.koin.android.ext.android.inject

class CategoryActivity : AppCompatActivity(), CategoryNavigator, CategoryItemNavigator {

    private val viewModelFactory: ViewModelProvider.Factory by inject()
    private lateinit var categoryViewModel: CategoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categoryViewModel =
            ViewModelProvider(this, viewModelFactory)[CategoryViewModel::class.java]

        subscribeToastMessage(categoryViewModel)

        val binding: ActivityCategoryBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_category)

        binding.lifecycleOwner = this
        binding.categoryVM = categoryViewModel
        binding.categoryNavigator = this
    }

    override fun onBackPressed() {
        val isEditMode = categoryViewModel.isEditMode.value
        if (isEditMode == true) {
            categoryViewModel.cancelEditCategories()
            return
        }
        super.onBackPressed()
    }

    override fun addNewCategory() {
        val intent = Intent(this, AddModifyCategoryActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_ADD_CATEGORY)
    }

    override fun filterWithImportant() {
        setResult(MEMO_IMPORTANT_FILTER_RESULT)
        finish()
    }

    override fun filterWithAll() {
        setResult(MEMO_ALL_FILTER_RESULT)
        finish()
    }

    override fun filterMemosWith(categoryId: String) {
        setResult(MEMO_CATEGORY_FILTER_RESULT)
        intent.putExtra("categoryId", categoryId)
        finish()
    }

    override fun modifyItem(categoryId: String) {
        val intent = Intent(this, AddModifyCategoryActivity::class.java)
        intent.putExtra("categoryId", categoryId)
        startActivityForResult(intent, REQUEST_CODE_MODIFY_CATEGORY)
    }

    private fun subscribeToastMessage(viewModel: CategoryViewModel) {
        viewModel.toastMessage.observe(this, Observer { stringId ->
            Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show()
        })
    }

    companion object {
        const val REQUEST_CODE_ADD_CATEGORY = 1000
        const val REQUEST_CODE_MODIFY_CATEGORY = 1001
    }
}

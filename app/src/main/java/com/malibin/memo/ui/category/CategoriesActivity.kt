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

class CategoriesActivity : AppCompatActivity(), CategoriesNavigator, CategoriesItemNavigator {

    private val viewModelFactory: ViewModelProvider.Factory by inject()
    private lateinit var categoriesViewModel: CategoriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categoriesViewModel =
            ViewModelProvider(this, viewModelFactory)[CategoriesViewModel::class.java]

        subscribeToastMessage(categoriesViewModel)

        val binding: ActivityCategoryBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_category)
        val categoryAdapter = createCategoriesAdapter()

        binding.apply {
            lifecycleOwner = this@CategoriesActivity
            categoriesVM = categoriesViewModel
            categoriesNavigator = this@CategoriesActivity
            rvCategories.adapter = categoryAdapter
        }
        subscribeCategories(categoriesViewModel, categoryAdapter)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AddModifyCategoryActivity.REQUEST_CODE) {
            categoriesViewModel.handleActivityResult(resultCode)
        }
    }

    override fun onBackPressed() {
        val isEditMode = categoriesViewModel.isEditMode.value
        if (isEditMode == true) {
            categoriesViewModel.cancelEditCategories()
            return
        }
        super.onBackPressed()
    }

    override fun addNewCategory() {
        val intent = Intent(this, AddModifyCategoryActivity::class.java)
        startActivityForResult(intent, AddModifyCategoryActivity.REQUEST_CODE)
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
        intent.putExtra("categoryId", categoryId)
        setResult(MEMO_CATEGORY_FILTER_RESULT, intent)
        finish()
    }

    override fun modifyItem(categoryId: String) {
        val intent = Intent(this, AddModifyCategoryActivity::class.java)
        intent.putExtra("categoryId", categoryId)
        startActivityForResult(intent, AddModifyCategoryActivity.REQUEST_CODE)
    }

    private fun createCategoriesAdapter(): CategoriesAdapter {
        val adapter = CategoriesAdapter().apply {
            categoriesViewModel = this@CategoriesActivity.categoriesViewModel
            lifeCycleOwner = this@CategoriesActivity
        }
        putItemClickListener(adapter)
        putItemDeleteClickListener(adapter)
        return adapter
    }

    private fun putItemClickListener(adapter: CategoriesAdapter) {
        adapter.setItemClickListener { categoryId ->
            val isEditMode = categoriesViewModel.isEditMode.value
            if (isEditMode == true) {
                this@CategoriesActivity.modifyItem(categoryId)
                return@setItemClickListener
            }
            this@CategoriesActivity.filterMemosWith(categoryId)
        }
    }

    private fun putItemDeleteClickListener(adapter: CategoriesAdapter) {
        adapter.setItemDeleteClickListener { categoryId ->
            categoriesViewModel.addCategoryToDelete(categoryId)
        }
    }

    private fun subscribeToastMessage(viewModel: CategoriesViewModel) {
        viewModel.toastMessage.observe(this, Observer { stringId ->
            Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show()
        })
    }

    private fun subscribeCategories(viewModel: CategoriesViewModel, adapter: CategoriesAdapter) {
        viewModel.items.observe(this, Observer {
            adapter.submitList(ArrayList(it))
        })
    }

    companion object{
        const val REQUEST_CODE = 1004
    }
}

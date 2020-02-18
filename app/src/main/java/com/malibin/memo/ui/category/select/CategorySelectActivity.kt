package com.malibin.memo.ui.category.select

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.malibin.memo.R
import com.malibin.memo.databinding.ActivityCategorySelectBinding
import com.malibin.memo.ui.category.CategoriesAdapter
import com.malibin.memo.ui.category.addmodify.AddModifyCategoryActivity
import com.malibin.memo.util.MEMO_CATEGORY_SELECTED
import org.koin.android.ext.android.inject

class CategorySelectActivity : AppCompatActivity(), CategorySelectNavigator {

    private val viewModelFactory: ViewModelProvider.Factory by inject()
    private lateinit var categorySelectViewModel: CategorySelectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categorySelectViewModel =
            ViewModelProvider(this, viewModelFactory)[CategorySelectViewModel::class.java]

        val binding: ActivityCategorySelectBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_category_select)

        val adapter = createCategoriesAdapter()

        binding.apply {
            categorySelectVM = categorySelectViewModel
            rvCategories.adapter = adapter
        }
        subscribeCategories(categorySelectViewModel, adapter)
        subscribeNewItemEvent(categorySelectViewModel)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        categorySelectViewModel.handleActivityResult(resultCode)
    }

    override fun onItemSelected(categoryId: String) {
        intent.putExtra("categoryId", categoryId)
        setResult(MEMO_CATEGORY_SELECTED)
        finish()
    }

    override fun addNewCategory() {
        val intent = Intent(this, AddModifyCategoryActivity::class.java)
        startActivityForResult(intent, AddModifyCategoryActivity.REQUEST_CODE)
    }

    private fun createCategoriesAdapter(): CategoriesAdapter {
        val adapter = CategoriesAdapter().apply {
            lifeCycleOwner = this@CategorySelectActivity
        }
        putItemClickListener(adapter)
        return adapter
    }

    private fun putItemClickListener(adapter: CategoriesAdapter) {
        adapter.setItemClickListener { categoryId ->
            this@CategorySelectActivity.onItemSelected(categoryId)
        }
    }

    private fun subscribeCategories(
        viewModel: CategorySelectViewModel,
        adapter: CategoriesAdapter
    ) {
        viewModel.items.observe(this, Observer {
            adapter.submitList(ArrayList(it))
        })
    }

    private fun subscribeNewItemEvent(viewModel: CategorySelectViewModel) {
        viewModel.newItemEvent.observe(this, Observer {
            this@CategorySelectActivity.addNewCategory()
        })
    }
}

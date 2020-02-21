package com.malibin.memo.ui.memo

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.malibin.memo.R
import com.malibin.memo.databinding.ActivityMemosBinding
import com.malibin.memo.db.entity.Category
import com.malibin.memo.ui.category.CategoriesActivity
import com.malibin.memo.util.DeployEvent
import org.koin.android.ext.android.inject

class MemosActivity : AppCompatActivity(), MemosNavigator {

    private val viewModelFactory: ViewModelProvider.Factory by inject()
    private lateinit var memosViewModel: MemosViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        memosViewModel = ViewModelProvider(this, viewModelFactory)[MemosViewModel::class.java]

        val memosAdapter = MemosAdapter(memosViewModel, this)

        val binding: ActivityMemosBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_memos)
        binding.apply {
            lifecycleOwner = this@MemosActivity
            memosVM = memosViewModel
            rvMemos.adapter = memosAdapter
        }

        subscribeMemos(memosAdapter)
        subscribeToastMessage()
        subscribeDeployEvent()
        subscribeCategoryColor()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        memosViewModel.handleActivityResult(requestCode, resultCode, data)
    }

    override fun filterCategory() {
        val intent = Intent(this, CategoriesActivity::class.java)
        startActivityForResult(intent, CategoriesActivity.REQUEST_CODE)
    }

    private fun subscribeMemos(memosAdapter: MemosAdapter) {
        memosViewModel.items.observe(this, Observer {
            memosAdapter.submitList(it)
        })
    }

    private fun subscribeToastMessage() {
        memosViewModel.toastMessage.observe(this, Observer { stringId ->
            Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show()
        })
    }

    private fun subscribeDeployEvent() {
        memosViewModel.deployEvent.observe(this, Observer {
            when (it.deployCode) {
                DeployEvent.FILTER_CATEGORY_ACT -> filterCategory()
            }
        })
    }

    private fun subscribeCategoryColor() {
        memosViewModel.filteredCategory.observe(this, Observer {
            if (it.colorCode.isEmpty()) {
                window.statusBarColor = Color.WHITE
                return@Observer
            }
            val categoryColor = Category.Color.valueOf(it.colorCode).resId
            window.statusBarColor = getColor(categoryColor)
        })
    }
}

package com.malibin.memo.ui.category

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.malibin.memo.R
import com.malibin.memo.databinding.ActivityAddModifyCategoryBinding
import org.koin.android.ext.android.inject

class AddModifyCategoryActivity : AppCompatActivity() {

    private val viewModelFactory: ViewModelProvider.Factory by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tossedCategoryId = getTossedCategoryId()
        val viewModel =
            ViewModelProvider(this, viewModelFactory)[AddModifyCategoryViewModel::class.java]
        viewModel.start(tossedCategoryId)

        subscribeSaveSuccess(viewModel)
        subscribeToastMessage(viewModel)

        val binding: ActivityAddModifyCategoryBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_add_modify_category)

        binding.addModifyCategoryVM = viewModel
        binding.lifecycleOwner = this
    }

    private fun subscribeToastMessage(viewModel: AddModifyCategoryViewModel) {
        viewModel.toastMessage.observe(this, Observer { stringId ->
            Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show()
        })
    }

    private fun subscribeSaveSuccess(viewModel: AddModifyCategoryViewModel) {
        viewModel.isSuccess.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        })
    }

    private fun getTossedCategoryId(): String? {
        return intent.getStringExtra("categoryId")
    }
}

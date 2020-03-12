package com.malibin.memo.ui.category.addmodify

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdRequest
import com.malibin.memo.R
import com.malibin.memo.databinding.ActivityAddModifyCategoryBinding
import com.malibin.memo.util.CATEGORY_SAVE_RESULT_OK
import org.koin.android.ext.android.inject

class AddModifyCategoryActivity : AppCompatActivity(), AddModifyCategoryNavigator {

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

        val adRequest = AdRequest.Builder().build()
        binding.banner.loadAd(adRequest)
    }

    override fun onCategorySaved() {
        setResult(CATEGORY_SAVE_RESULT_OK)
        finish()
    }

    private fun subscribeToastMessage(viewModel: AddModifyCategoryViewModel) {
        viewModel.toastMessage.observe(this, Observer { stringId ->
            Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show()
        })
    }

    private fun subscribeSaveSuccess(viewModel: AddModifyCategoryViewModel) {
        viewModel.isSuccess.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                this@AddModifyCategoryActivity.onCategorySaved()
            }
        })
    }

    private fun getTossedCategoryId(): String? {
        return intent.getStringExtra("categoryId")
    }

    companion object {
        const val REQUEST_CODE = 1000
    }
}

package com.malibin.memo.ui.memo.edit

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.malibin.memo.R
import com.malibin.memo.databinding.ActivityMemoEditBinding
import com.malibin.memo.ui.category.select.CategorySelectActivity
import com.malibin.memo.util.MEMO_DELETED
import com.malibin.memo.util.MEMO_EDIT_CANCELED
import com.malibin.memo.util.MEMO_SAVED
import org.koin.android.ext.android.inject

class MemoEditActivity : AppCompatActivity(), MemoEditNavigator {

    private val viewModelFactory: ViewModelProvider.Factory by inject()
    private lateinit var memoEditViewModel: MemoEditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tossedMemoId = getTossedMemoId()
        memoEditViewModel = ViewModelProvider(this, viewModelFactory)[MemoEditViewModel::class.java]
        memoEditViewModel.start(tossedMemoId)

        val pagerAdapter = MemoImagePagerAdapter(this)

        val binding: ActivityMemoEditBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_memo_edit)

        binding.apply {
            memoEditVM = memoEditViewModel
            lifecycleOwner = this@MemoEditActivity
            vpMemoImages.adapter = pagerAdapter
        }
        subscribeViewModel(pagerAdapter)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        memoEditViewModel.handleActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        onEditMemoCanceled()
    }

    override fun onMemoSaved() {
        setResult(MEMO_SAVED)
        finish()
    }

    override fun onMemoDeleted() {
        setResult(MEMO_DELETED)
        finish()
    }

    override fun onEditMemoCanceled() {
        setResult(MEMO_EDIT_CANCELED)
        finish()
    }

    override fun selectCategory() {
        val intent = Intent(this, CategorySelectActivity::class.java)
        startActivityForResult(intent, CategorySelectActivity.REQUEST_CODE)
    }

    private fun getTossedMemoId(): String? {
        return intent.getStringExtra("memoId")
    }

    private fun subscribeViewModel(adapter: MemoImagePagerAdapter) {
        subscribeToastMessage()
        subscribeEditCancelEvent()
        subscribeDeploySelectCategoryEvent()
        subscribeSaveSuccess()
        subscribeDeletedMemo()
        subscribeImages(adapter)
    }

    private fun subscribeEditCancelEvent() {
        memoEditViewModel.editCancelEvent.observe(this, Observer {
            this@MemoEditActivity.onEditMemoCanceled()
        })
    }

    private fun subscribeDeploySelectCategoryEvent() {
        memoEditViewModel.deploySelectCategoryEvent.observe(this, Observer {
            this@MemoEditActivity.selectCategory()
        })
    }

    private fun subscribeToastMessage() {
        memoEditViewModel.toastMessage.observe(this, Observer { stringId ->
            Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show()
        })
    }

    private fun subscribeSaveSuccess() {
        memoEditViewModel.isSuccess.observe(this, Observer {
            this@MemoEditActivity.onMemoSaved()
        })
    }

    private fun subscribeDeletedMemo() {
        memoEditViewModel.isDeleted.observe(this, Observer {
            this@MemoEditActivity.onMemoDeleted()
        })
    }

    private fun subscribeImages(adapter: MemoImagePagerAdapter) {
        memoEditViewModel.shownImages.observe(this, Observer {
            adapter.submitList(it)
        })
    }
}

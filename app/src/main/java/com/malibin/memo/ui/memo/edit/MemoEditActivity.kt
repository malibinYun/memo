package com.malibin.memo.ui.memo.edit

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.malibin.memo.R
import com.malibin.memo.databinding.ActivityMemoEditBinding
import com.malibin.memo.db.entity.Image
import com.malibin.memo.ui.category.select.CategorySelectActivity
import com.malibin.memo.ui.memo.dialog.MemoImageDialog
import com.malibin.memo.util.DeployEvent
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

        val pagerAdapter = MemoImagePagerAdapter(this).apply {
            setAddImageClickListener { deployMemoImageDialog() }
            setImageClickListener { onImageClick(it) }
            setDeleteClickListener { onImageDeleteClick(it) }
        }
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        memoEditViewModel.handleRequestPermissionsResult(requestCode, permissions, grantResults)
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

    private fun onImageDeleteClick(image: Image) {
        memoEditViewModel.deleteImage(image)
    }

    private fun onImageClick(image: Image) {

    }

    private fun deployMemoImageDialog() {
        MemoImageDialog(this, memoEditViewModel).show()
    }

    private fun deployCameraOrRequestPermission() {
        if (isPermissionGrantedOf(Manifest.permission.CAMERA) &&
            isPermissionGrantedOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ) {
            deployCamera()
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_CODE_CAMERA_PERMISSION
        )
    }

    private fun deployCamera() {
        val values = ContentValues()
        val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA)
        memoEditViewModel.externalCameraUri = imageUri
    }

    private fun deployGalleryOrRequestPermission() {
        if (isPermissionGrantedOf(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            deployGallery()
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        requestPermissions(
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_CODE_GALLERY_PERMISSION
        )
    }

    private fun deployGallery() {
        val photoPickerIntent = Intent()
        photoPickerIntent.apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        startActivityForResult(
            Intent.createChooser(photoPickerIntent, "select picture"),
            REQUEST_CODE_PICK_IMAGES
        )
    }

    private fun isPermissionGrantedOf(permission: String): Boolean {
        val previousPermissionGranted =
            ActivityCompat.checkSelfPermission(this, permission)
        return previousPermissionGranted == PackageManager.PERMISSION_GRANTED
    }

    private fun subscribeViewModel(adapter: MemoImagePagerAdapter) {
        subscribeToastMessage()
        subscribeEditCancelEvent()
        subscribeDeployEvent()
        subscribeSaveSuccess()
        subscribeDeletedMemo()
        subscribeImages(adapter)
    }

    private fun subscribeEditCancelEvent() {
        memoEditViewModel.editCancelEvent.observe(this, Observer {
            this@MemoEditActivity.onEditMemoCanceled()
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

    private fun subscribeDeployEvent() {
        memoEditViewModel.deployEvent.observe(this, Observer {
            when (it.deployCode) {
                DeployEvent.GALLERY -> deployGalleryOrRequestPermission()
                DeployEvent.CAMERA -> deployCameraOrRequestPermission()
                DeployEvent.SELECT_CATEGORY_ACT -> selectCategory()
            }
        })
    }

    companion object {
        const val REQUEST_CODE_GALLERY_PERMISSION = 1002
        const val REQUEST_CODE_PICK_IMAGES = 1003
        const val REQUEST_CODE_CAMERA_PERMISSION = 1005
        const val REQUEST_CODE_CAMERA = 1006
        const val REQUEST_CODE = 1007
    }
}

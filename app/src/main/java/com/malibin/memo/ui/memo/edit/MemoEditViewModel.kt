package com.malibin.memo.ui.memo.edit

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.malibin.memo.R
import com.malibin.memo.db.CategoryRepository
import com.malibin.memo.db.MemoRepository
import com.malibin.memo.db.entity.Category
import com.malibin.memo.db.entity.Image
import com.malibin.memo.db.entity.Memo
import com.malibin.memo.ui.category.select.CategorySelectActivity
import com.malibin.memo.ui.memo.edit.MemoEditActivity.Companion.REQUEST_CODE_PICK_IMAGES
import com.malibin.memo.ui.util.ImageLoader
import com.malibin.memo.util.BaseViewModel
import com.malibin.memo.util.DeployEvent
import com.malibin.memo.util.MEMO_CATEGORY_SELECTED
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class MemoEditViewModel(
    private val memoRepository: MemoRepository,
    private val categoryRepository: CategoryRepository
) : BaseViewModel() {

    val title = MutableLiveData<String>()

    val content = MutableLiveData<String>()

    val isImportant = MutableLiveData<Boolean>()

    val category = MutableLiveData<Category>()

    val shownImagePosition = MutableLiveData<Int>().apply { value = 1 }

    private val originalImages = mutableListOf<Image>()

    private val _shownImages = MutableLiveData<List<Image>>()
    val shownImages: LiveData<List<Image>>
        get() = _shownImages

    private val _isImageLoading = MutableLiveData<Boolean>()
    val isImageLoading: LiveData<Boolean>
        get() = _isImageLoading

    private val _createdDate = MutableLiveData<Long>().apply { value = System.currentTimeMillis() }
    val createdDate: LiveData<Long>
        get() = _createdDate

    private val _editCancelEvent = MutableLiveData<Boolean>()
    val editCancelEvent: LiveData<Boolean>
        get() = _editCancelEvent

    private val _deployEvent = MutableLiveData<DeployEvent>()
    val deployEvent: LiveData<DeployEvent>
        get() = _deployEvent

    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean>
        get() = _isDeleted

    private var memoId: String? = null

    private var isNewMemo: Boolean = true

    private lateinit var imageLoader: ImageLoader
    private var asyncCount = 0

    fun start(memoId: String?) {
        _isLoading.value = true
        this.memoId = memoId

        if (memoId == null) {
            isNewMemo = true
            this.memoId = UUID.randomUUID().toString()
            imageLoader = ImageLoader(this.memoId!!)
            loadCategory(Category.BASIC_ID)
            _isLoading.value = false
            return
        }
        isNewMemo = false
        loadMemo(memoId)
        imageLoader = ImageLoader(memoId)
    }

    private fun loadMemo(memoId: String) {
        memoRepository.getMemo(memoId) {
            val memo = it ?: throw RuntimeException("memo exist but cannot loaded")
            loadMemo(memo)
        }
    }

    private fun loadMemo(memo: Memo) {
        title.value = memo.title
        content.value = memo.content
        _shownImages.value = memo.getImages()
        originalImages.addAll(memo.getImages())
        isImportant.value = memo.isImportant
        _createdDate.value = memo.createdDate
        loadCategory(memo.categoryId)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CategorySelectActivity.REQUEST_CODE) {
            if (resultCode == MEMO_CATEGORY_SELECTED) {
                val categoryId = data?.getStringExtra("categoryId") ?: Category.BASIC_ID
                loadCategory(categoryId)
            }
        }
        if (requestCode == REQUEST_CODE_PICK_IMAGES) {
            if (resultCode == Activity.RESULT_OK) {
                data as Intent
                loadImageFromIntent(data)
            }
        }
    }

    fun handleRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MemoEditActivity.REQUEST_CODE_GALLERY_PERMISSION) {
            val isPermissionGranted =
                grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (isPermissionGranted) {
                _deployEvent.value = DeployEvent(DeployEvent.GALLERY)
                return
            }
            _toastMessage.value = R.string.gallery_permission_rejected
        }
    }

    fun deploySelectCategory() {
        _deployEvent.value = DeployEvent(DeployEvent.SELECT_CATEGORY_ACT)
    }

    fun deployGallery() {
        _deployEvent.value = DeployEvent(DeployEvent.GALLERY)
    }

    private fun loadCategory(categoryId: String) {
        categoryRepository.getCategory(categoryId) {
            val category = it ?: throw RuntimeException("category exist but cannot loaded")
            this.category.value = category
        }
    }

    private fun loadImageFromIntent(intent: Intent) {
        _isImageLoading.value = true
        val isOneImagePicked = intent.data != null
        if (isOneImagePicked) {
            loadOneImage(intent)
            return
        }
        loadImages(intent)
    }

    private fun loadOneImage(intent: Intent) {
        val imageUri = intent.data ?: throw RuntimeException("intent data is null")
        asyncCount = 1
        imageLoader.getImage(imageUri) { finishLoadImage(it) }
    }

    private fun loadImages(intent: Intent) {
        val clipData = intent.clipData ?: throw RuntimeException("intent clip data is null")
        val uriCount = clipData.itemCount
        asyncCount = uriCount
        for (i in 0 until uriCount) {
            val uri = clipData.getItemAt(i).uri
            imageLoader.getImage(uri) { finishLoadImage(it) }
        }
    }

    private fun finishLoadImage(image: Image) {
        asyncCount--
        addImage(image)
        if (asyncCount == 0) _isImageLoading.value = false
    }

    private fun addImage(image: Image) {
        val shownImages = _shownImages.value ?: listOf()
        val newImages = ArrayList(shownImages).apply { add(image) }
        _shownImages.value = newImages
    }

    fun deleteImage(image: Image) {
        val shownImages = _shownImages.value ?: listOf()
        val newImages = ArrayList(shownImages).apply { remove(image) }
        _shownImages.value = newImages
    }

    fun cancelEditMemo() {
        _editCancelEvent.value = true
    }

    fun deleteMemo() {
        if (isNewMemo) {
            _editCancelEvent.value = true
            return
        }
        val currentMemoId = memoId ?: throw RuntimeException("memo isn't new but don't have Id")
        memoRepository.deleteMemoById(currentMemoId)
        _isDeleted.value = true
    }

    fun saveMemo() {
        val isTitleEmpty = title.value.isNullOrBlank()
        val isContentEmpty = content.value.isNullOrBlank()
        val isImageEmpty = _shownImages.value.isNullOrEmpty()

        if (isTitleEmpty) {
            _toastMessage.value = R.string.input_title
            return
        }
        if (isContentEmpty && isImageEmpty) {
            _toastMessage.value = R.string.input_content
            return
        }
        if (isNewMemo) {
            addMemo()
            return
        }
        modifyMemo()
    }

    private fun addMemo() {
        val memo = assembleMemo()
        memoRepository.saveMemo(memo)
        _isSuccess.value = true
    }

    private fun modifyMemo() {
        val memo = assembleMemo()
        val imageToDelete = getImagesToDelete()
        memoRepository.modifyMemo(memo, imageToDelete)
        _isSuccess.value = true
    }

    private fun assembleMemo(): Memo {
        val memo: Memo
        val currentId = memoId ?: throw RuntimeException("Cannot get memo id")
        val createdDate = _createdDate.value ?: throw RuntimeException("Cannot get created time")

        memo = Memo(id = currentId, createdDate = createdDate)
        injectFieldsInMemo(memo)
        return memo
    }

    private fun injectFieldsInMemo(memo: Memo) {
        memo.title = this.title.value ?: throw RuntimeException("Cannot get title")
        memo.content = this.content.value ?: throw RuntimeException("Cannot get content")
        memo.categoryId =
            this.category.value?.id ?: throw RuntimeException("Cannot get categoryId")
        val images = this._shownImages.value ?: listOf()
        memo.addImages(images)
    }

    private fun getImagesToDelete(): List<Image> {
        val imagesToDelete = ArrayList(originalImages)
        val shownImages = _shownImages.value ?: listOf()
        imagesToDelete.removeAll(shownImages)
        return imagesToDelete
    }

}
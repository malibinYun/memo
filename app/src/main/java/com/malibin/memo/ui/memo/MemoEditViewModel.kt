package com.malibin.memo.ui.memo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.malibin.memo.R
import com.malibin.memo.db.CategoryRepository
import com.malibin.memo.db.MemoRepository
import com.malibin.memo.db.entity.Category
import com.malibin.memo.db.entity.Image
import com.malibin.memo.db.entity.Memo
import com.malibin.memo.util.BaseViewModel
import java.lang.RuntimeException

class MemoEditViewModel(
    private val memoRepository: MemoRepository,
    private val categoryRepository: CategoryRepository
) : BaseViewModel() {

    val title = MutableLiveData<String>()

    val content = MutableLiveData<String>()

    val isImportant = MutableLiveData<Boolean>()

    val category = MutableLiveData<Category>()

    private val originalImages = mutableListOf<Image>()

    private val _shownImages = MutableLiveData<List<Image>>()
    val shownImages: LiveData<List<Image>>
        get() = _shownImages

    private val _createdDate = MutableLiveData<Long>().apply { value = System.currentTimeMillis() }
    val createdDate: LiveData<Long>
        get() = _createdDate

    private val _editCancelEvent = MutableLiveData<Boolean>()
    val editCancelEvent: LiveData<Boolean>
        get() = _editCancelEvent

    private val _deploySelectCategoryEvent = MutableLiveData<Boolean>()
    val deploySelectCategoryEvent: LiveData<Boolean>
        get() = _deploySelectCategoryEvent

    private var memoId: String? = null

    private var isNewMemo: Boolean = true

    fun start(memoId: String?) {
        _isLoading.value = true
        this.memoId = memoId

        if (memoId == null) {
            isNewMemo = true
            loadCategory(Category.BASIC_ID)
            _isLoading.value = false
            return
        }
        isNewMemo = false
        loadMemo(memoId)
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

    fun deploySelectCategory() {
        _deploySelectCategoryEvent.value = true
    }

    fun updateCategory(categoryId: String) {
        loadCategory(categoryId)
    }

    private fun loadCategory(categoryId: String) {
        categoryRepository.getCategory(categoryId) {
            val category = it ?: throw RuntimeException("category exist but cannot loaded")
            this.category.value = category
        }
    }

    fun addImage(image: Image) {
        val shownImages = _shownImages.value ?: listOf()
        val newImages = ArrayList(shownImages).apply { add(image) }
        _shownImages.value = newImages
    }

    fun addImages(images: List<Image>) {
        val shownImages = _shownImages.value ?: listOf()
        val newImages = ArrayList(shownImages).apply { addAll(images) }
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
    }

    private fun modifyMemo() {
        val memo = assembleMemo()
        val imageToDelete = getImagesToDelete()
        memoRepository.modifyMemo(memo, imageToDelete)
    }

    private fun assembleMemo(): Memo {
        val memo: Memo
        val currentId = memoId
        val createdDate = _createdDate.value ?: throw RuntimeException("Cannot get created time")
        if (currentId == null) {
            memo = Memo(createdDate = createdDate)
            injectFieldsInMemo(memo)
            return memo
        }
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
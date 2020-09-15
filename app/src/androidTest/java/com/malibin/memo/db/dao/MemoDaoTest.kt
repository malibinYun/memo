package com.malibin.memo.db.dao

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.malibin.memo.db.AppDatabase
import com.malibin.memo.db.entity.Category
import com.malibin.memo.db.entity.Image
import com.malibin.memo.db.entity.Memo
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class MemoDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var memoDao: MemoDao

    @Before
    fun initMemoDao() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java
        ).build()
        database.categoryDao().insertCategory(DEFAULT_CATEGORY)
        database.categoryDao().insertCategory(NEW_CATEGORY)
        memoDao = database.memoDao()
    }

    @Test
    fun insertMemo_getAllMemosNoImages() {
        memoDao.insertMemoNoImages(DEFAULT_MEMO)

        val retrievesMemos = memoDao.getMemosNoImages()

        assertEquals(1, retrievesMemos.size)
        assertEquals(DEFAULT_MEMO_NO_IMAGES, retrievesMemos[0])
    }

    @Test
    fun insertMemo_getMemoNoImagesById() {
        memoDao.insertMemoNoImages(DEFAULT_MEMO)

        val retrievesMemo = memoDao.getMemoNoImagesById(DEFAULT_ID)

        assertEquals(DEFAULT_MEMO_NO_IMAGES, retrievesMemo)
    }

    @Test
    fun insertMemo_getImagesByMemoId() {
        memoDao.saveMemo(DEFAULT_MEMO)

        val retrievesImagesOfMemo = memoDao.getImagesByMemoId(DEFAULT_ID)

        assertEquals(DEFAULT_IMAGES, retrievesImagesOfMemo)
    }

    @Test
    fun insertMemo_replacesOnConflict() {
        memoDao.insertMemoNoImages(DEFAULT_MEMO)

        val newMemoSameId = Memo(
            id = DEFAULT_ID,
            title = NEW_TITLE,
            categoryId = NEW_CATEGORY_ID,
            content = NEW_CONTENT
        ).apply { addImages(DEFAULT_IMAGES) }
        memoDao.insertMemoNoImages(newMemoSameId)

        val retrievesMemo = memoDao.getMemoNoImagesById(DEFAULT_ID)

        assertEquals(newMemoSameId.apply { clearOnlyImageMutableList() }, retrievesMemo)
    }

    @Test
    fun insertImages_triggerForeignKeyConstraint() {
        try {
            memoDao.insertImages(listOf(DEFAULT_IMAGE1))
            Assert.fail("외래키 제약 조건으로 Exception 발생해야함")
        } catch (e: SQLiteConstraintException) {
            // Success!!
        }
    }

    @Test
    fun insertImages_replacesOnConflict() {
        memoDao.saveMemo(DEFAULT_MEMO)

        val newImage1 = DEFAULT_IMAGE1.copy().apply { byteCode = byteArrayOf(8, 8, 8) }
        val newImage2 = DEFAULT_IMAGE2.copy().apply { byteCode = byteArrayOf(9, 9, 9) }
        val newImages = listOf(newImage1, newImage2)

        memoDao.insertImages(newImages)

        val retrievesImages = memoDao.getImagesByMemoId(DEFAULT_ID)
        assertEquals(newImages, retrievesImages)
    }

    @Test
    fun updateMemo_getById() {
        memoDao.insertMemoNoImages(DEFAULT_MEMO)

        val updatedMemo = DEFAULT_MEMO_NO_IMAGES.copy().apply {
            title = NEW_TITLE
            categoryId = NEW_CATEGORY_ID
            content = NEW_CONTENT
            isImportant = true
        }
        memoDao.updateMemo(updatedMemo)

        val retrievedMemo = memoDao.getMemoNoImagesById(DEFAULT_ID)
        assertEquals(updatedMemo, retrievedMemo)
    }

    @Test
    fun deleteMemo_getById() {
        memoDao.insertMemoNoImages(DEFAULT_MEMO)

        memoDao.deleteMemoById(DEFAULT_ID)

        val deletedMemo: Memo? = memoDao.getMemoNoImagesById(DEFAULT_ID)

        assertNull(deletedMemo)
    }

    @Test
    fun deleteImages_getImages() {
        memoDao.saveMemo(DEFAULT_MEMO)

        memoDao.deleteImages(DEFAULT_IMAGES)

        val retrievedImages = memoDao.getImagesByMemoId(DEFAULT_ID)

        assertEquals(0, retrievedImages.size)
    }

    @Test
    fun saveMemo_getMemoWithImages() {
        memoDao.saveMemo(DEFAULT_MEMO)

        val retrievedImages = memoDao.getImagesByMemoId(DEFAULT_ID)
        val retrievedMemoNoImages = memoDao.getMemoNoImagesById(DEFAULT_ID)
        val retrievedMemo = retrievedMemoNoImages?.apply { loadImages(retrievedImages) }

        assertEquals(DEFAULT_MEMO, retrievedMemo)
    }

    @Test
    fun modifyMemo_getMemoWithImages() {
        memoDao.saveMemo(DEFAULT_MEMO)

        val updatedMemo = DEFAULT_MEMO.copy().apply {
            title = NEW_TITLE
            categoryId = NEW_CATEGORY_ID
            content = NEW_CONTENT
            isImportant = true
            addImages(listOf(DEFAULT_IMAGE2, DEFAULT_IMAGE3))
        }
        val deletedImages = listOf(DEFAULT_IMAGE1)

        memoDao.modifyMemo(updatedMemo, deletedImages)

        val retrievedImages = memoDao.getImagesByMemoId(DEFAULT_ID)
        val retrievedMemoNoImages = memoDao.getMemoNoImagesById(DEFAULT_ID)
        val retrievedMemo = retrievedMemoNoImages?.apply { loadImages(retrievedImages) }

        assertEquals(updatedMemo, retrievedMemo)
    }

    companion object {
        private const val DEFAULT_ID = "default id"

        private val DEFAULT_IMAGE1 = Image(ofMemoId = DEFAULT_ID, byteCode = byteArrayOf(1, 1, 1))
        private val DEFAULT_IMAGE2 = Image(ofMemoId = DEFAULT_ID, byteCode = byteArrayOf(2, 2, 2))
        private val DEFAULT_IMAGE3 = Image(ofMemoId = DEFAULT_ID, byteCode = byteArrayOf(3, 3, 3))
        private val DEFAULT_IMAGES = listOf(DEFAULT_IMAGE1, DEFAULT_IMAGE2)

        private const val DEFAULT_TITLE = "default memo"
        private const val DEFAULT_CATEGORY_ID = "default category id"
        private const val DEFAULT_CONTENT = "default content"
        private val DEFAULT_MEMO = Memo(
            id = DEFAULT_ID,
            title = DEFAULT_TITLE,
            categoryId = DEFAULT_CATEGORY_ID,
            content = DEFAULT_CONTENT
        ).apply { addImages(DEFAULT_IMAGES) }

        private val DEFAULT_MEMO_NO_IMAGES = DEFAULT_MEMO.copy().apply {
            addImages(DEFAULT_IMAGES)
            clearOnlyImageMutableList()
        }

        private const val NEW_TITLE = "new memo"
        private const val NEW_CATEGORY_ID = "new category id"
        private const val NEW_CONTENT = "new content"

        private const val DEFAULT_NAME = "default category"
        private val DEFAULT_CATEGORY = Category(id = DEFAULT_CATEGORY_ID, name = DEFAULT_NAME)
        private val NEW_CATEGORY = Category(id = NEW_CATEGORY_ID, name = DEFAULT_NAME)
    }
}
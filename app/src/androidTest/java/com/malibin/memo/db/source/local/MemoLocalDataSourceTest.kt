package com.malibin.memo.db.source.local

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.test.platform.app.InstrumentationRegistry
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.malibin.memo.R
import com.malibin.memo.db.entity.Image
import com.malibin.memo.db.entity.Memo
import com.malibin.memo.util.toByteArray
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import java.util.concurrent.CountDownLatch

class MemoLocalDataSourceTest : KoinTest {

    private val localDataSource: MemoLocalDataSource by inject()
    private var insertedMemoId = ""

    private lateinit var countDownLatch: CountDownLatch

    @Before
    fun prepareCountDownLatch() {
        countDownLatch = CountDownLatch(1)
    }

    @After
    fun deleteCreatedMemo() {
        localDataSource.deleteMemoById(insertedMemoId)
    }

    @Test(timeout = 2000)
    fun saveMemoNoImage_retrievesMemo() {
        val newMemo = Memo(title = "test", content = "test content")
        var retrievedMemo: Memo? = null

        localDataSource.saveMemo(newMemo)
        insertedMemoId = newMemo.id

        localDataSource.getMemo(newMemo.id) {
            retrievedMemo = it
            countDownLatch.countDown()
        }
        countDownLatch.await()
        assertEquals(newMemo, retrievedMemo)
    }

    @Test(timeout = 2000)
    fun saveMemoOneImage_retrievesMemo() {
        var newMemo = Memo()
        var retrievedMemo: Memo? = null
        createMemoOneImage {
            newMemo = it
            countDownLatch.countDown()
        }
        countDownLatch.await()
        prepareCountDownLatch()

        localDataSource.saveMemo(newMemo)
        insertedMemoId = newMemo.id

        localDataSource.getMemo(newMemo.id) {
            retrievedMemo = it
            countDownLatch.countDown()
        }
        countDownLatch.await()
        assertEquals(newMemo, retrievedMemo)
        assertEquals(newMemo.getImages(), retrievedMemo?.getImages())
    }

    @Test(timeout = 2000)
    fun deleteMemo_cascade_deleteImages() {
        var newMemo = Memo()
        val images: MutableList<Image> = ArrayList()
        createMemoOneImage {
            newMemo = it
            countDownLatch.countDown()
        }
        countDownLatch.await()
        prepareCountDownLatch()

        localDataSource.saveMemo(newMemo)
        insertedMemoId = newMemo.id

        localDataSource.deleteMemoById(newMemo.id)

        localDataSource.getImagesOfMemo(newMemo.id) {
            images.addAll(it)
            countDownLatch.countDown()
        }
        countDownLatch.await()
        assertEquals(emptyList<Image>(), images)
    }

    @Test(timeout = 2000)
    fun modifyMemo_deleteImages() {
        var newMemo = Memo()
        var retrievedMemo: Memo? = null
        val deletedImages: MutableList<Image> = ArrayList()
        createMemoThreeImage {
            newMemo = it
            insertedMemoId = it.id
            localDataSource.saveMemo(it)
            countDownLatch.countDown()
        }
        countDownLatch.await()
        prepareCountDownLatch()

        deletedImages.add(newMemo.removeImageAt(0))
        deletedImages.add(newMemo.removeImageAt(1))
        localDataSource.modifyMemo(newMemo, deletedImages)

        localDataSource.getMemo(newMemo.id) {
            retrievedMemo = it
            countDownLatch.countDown()
        }
        countDownLatch.await()
        assertEquals(newMemo, retrievedMemo)
        // 썸네일 제대로 바뀌는지 검사
        assertArrayEquals(newMemo.thumbnailByteCode, retrievedMemo!!.getImages()[0].byteCode)
    }

    @Test(timeout = 2000)
    fun modifyMemo_deleteAllImages() {
        var newMemo = Memo()
        var retrievedMemo: Memo? = null
        val deletedImages: MutableList<Image> = ArrayList()
        createMemoThreeImage {
            newMemo = it
            insertedMemoId = it.id
            localDataSource.saveMemo(it)
            countDownLatch.countDown()
        }
        countDownLatch.await()
        prepareCountDownLatch()

        deletedImages.add(newMemo.removeImageAt(0))
        deletedImages.add(newMemo.removeImageAt(0))
        deletedImages.add(newMemo.removeImageAt(0))
        localDataSource.modifyMemo(newMemo, deletedImages)

        localDataSource.getMemo(newMemo.id) {
            retrievedMemo = it
            countDownLatch.countDown()
        }
        countDownLatch.await()
        assertEquals(newMemo, retrievedMemo)
    }

    @Test(timeout = 2000)
    fun verifyDeleteMemo() {
        var newMemo = Memo()
        var deletedMemo: Memo? = Memo(content = "Not Deleted")
        createMemoOneImage {
            newMemo = it
            insertedMemoId = it.id
            localDataSource.saveMemo(it)
            countDownLatch.countDown()
        }
        countDownLatch.await()
        prepareCountDownLatch()

        localDataSource.deleteMemoById(newMemo.id)

        localDataSource.getMemo(newMemo.id) {
            deletedMemo = it
            countDownLatch.countDown()
        }
        countDownLatch.await()
        assertNull(deletedMemo)
    }

    @Test(timeout = 2000)
    fun saveMemo_retrievesImages() {
        var newMemo = Memo()
        val retrievedImages: MutableList<Image> = ArrayList()
        createMemoThreeImage {
            newMemo = it
            insertedMemoId = it.id
            localDataSource.saveMemo(it)
            countDownLatch.countDown()
        }
        countDownLatch.await()
        prepareCountDownLatch()

        localDataSource.getImagesOfMemo(newMemo.id) {
            retrievedImages.addAll(it)
            countDownLatch.countDown()
        }
        countDownLatch.await()
        assertEquals(newMemo.getImages(), retrievedImages)
    }

    private fun createMemoOneImage(
        title: String = "test",
        content: String = "test content",
        categoryId: String = "",
        @DrawableRes imageResource: Int = R.drawable.brown,
        callback: (memo: Memo) -> Unit
    ) {
        createBitmap(imageResource = imageResource) {
            val newMemo = Memo(title = title, content = content, categoryId = categoryId)
            val image = Image(ofMemoId = newMemo.id, byteCode = it.toByteArray())
            newMemo.apply { addImage(image) }
            callback(newMemo)
        }
    }

    private fun createMemoThreeImage(callback: (memo: Memo) -> Unit) {
        val newMemo = Memo()
        createBitmap(R.drawable.cony) { conyBitmap ->
            createBitmap(R.drawable.sally) { sallyBitmap ->
                createBitmap { brownBitmap ->
                    val images = listOf(
                        Image(ofMemoId = newMemo.id, byteCode = conyBitmap.toByteArray()),
                        Image(ofMemoId = newMemo.id, byteCode = sallyBitmap.toByteArray()),
                        Image(ofMemoId = newMemo.id, byteCode = brownBitmap.toByteArray())
                    )
                    newMemo.apply { addImages(images) }
                    callback(newMemo)
                }
            }
        }
    }

    private fun createBitmap(
        @DrawableRes imageResource: Int = R.drawable.brown,
        callback: (bitmap: Bitmap) -> Unit
    ) {
        Glide.with(InstrumentationRegistry.getInstrumentation().context)
            .asBitmap()
            .load(imageResource)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {}

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    callback(resource)
                }
            })
    }

}
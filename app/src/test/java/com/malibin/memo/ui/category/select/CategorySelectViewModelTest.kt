package com.malibin.memo.ui.category.select

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.malibin.memo.db.CategoryRepository
import com.malibin.memo.db.entity.Category
import com.malibin.memo.takeValue
import com.malibin.memo.util.CATEGORY_SAVE_RESULT_OK
import com.nhaarman.mockitokotlin2.argumentCaptor
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.verify

class CategorySelectViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var categoryRepository: CategoryRepository

    private val categoriesCallbackCaptor = argumentCaptor<(categories: List<Category>) -> Unit>()

    private lateinit var viewModel: CategorySelectViewModel
    private lateinit var categories: List<Category>

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        viewModel = CategorySelectViewModel(categoryRepository)

        categories = createCategories()
    }

    private fun createCategories(): List<Category> {
        val category1 = Category(Category.BASIC_ID, "내 메모")
        val category2 = Category("id2", "1", Category.Color.GREEN.name)
        val category3 = Category("id3", "2", Category.Color.BLUE.name)
        return listOf(category1, category2, category3)
    }

    @Test
    fun `Viewmodel 초기화가 잘 이루어지는지 확인`() {
        // given
        // ViewModel이 초기화 되었을 때 (getAllCategories의 callback이 실행되지 않은상태)
        viewModel.start()

        // callback이 실행되지 않아 loading 중인 상태여야함
        assertEquals(viewModel.isLoading.takeValue(), true)

        // when
        // getAllCategories의 Callback을 잡아 invoke 시키면,
        verify<CategoryRepository>(categoryRepository).getAllCategories(categoriesCallbackCaptor.capture())
        categoriesCallbackCaptor.firstValue.invoke(categories)

        // then
        // loading 끝나고, 데이터 상태 검증.
        assertEquals(viewModel.isLoading.takeValue(), false)
        assertEquals(viewModel.items.takeValue()?.size, 3)
    }

    @Test
    fun `카테고리 추가 이벤트 발생하는지 테스트`() {
        // when
        // 카테고리 추가 이벤트 발생
        viewModel.addNewCategoryEvent()

        // then
        // 이벤트 LiveData 갱신되었는지 확인
        assertEquals(viewModel.newItemEvent.takeValue(), true)
    }

    @Test
    fun `onActivityResult 핸들러 작동 확인`() {
        // when
        // 핸들러 이벤트 발생
        viewModel.handleActivityResult(CATEGORY_SAVE_RESULT_OK)

        // then
        // loadCategories안의 categoryRepository.getAllCategories() 메소드 실행 확인
        // 및 로딩, 아이템 LiveData 확인
        verify(categoryRepository).getAllCategories(categoriesCallbackCaptor.capture())
        categoriesCallbackCaptor.firstValue.invoke(categories)

        assertEquals(viewModel.isLoading.takeValue(), false)
        assertEquals(viewModel.items.takeValue()?.size, 3)
    }

}
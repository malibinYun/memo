package com.malibin.memo.ui.category.addmodify

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.malibin.memo.R
import com.malibin.memo.db.CategoryRepository
import com.malibin.memo.db.entity.Category
import com.malibin.memo.takeValue
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class AddModifyCategoryViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var categoryRepository: CategoryRepository

    private val categoryCallbackCaptor = argumentCaptor<(category: Category?) -> Unit>()

    private lateinit var viewModel: AddModifyCategoryViewModel
    private lateinit var category: Category

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        category = Category(CATEGORY_ID, CATEGORY_NAME, CATEGORY_COLOR.name)

        viewModel = AddModifyCategoryViewModel(categoryRepository)
    }

    @Test
    fun `새 카테고리 ViewModel 초기화 시 동작 테스트`() {
        // given when
        // category Id를 null값으로 초기화
        viewModel.start(null)

        // then
        // 불러올 Category가 없으니 loading 해제
        assertEquals(viewModel.isLoading.takeValue(), false)

        // getCategory 메소드는 실행되지 않음.
        verify(categoryRepository, never()).getCategory(any(), any())
    }

    @Test
    fun `새 카테고리 저장 시 이름 없을 때 테스트`() {
        // given
        // category Id를 null값으로 초기화
        viewModel.start(null)

        // 불러올 Category가 없으니 loading 해제
        assertEquals(viewModel.isLoading.takeValue(), false)

        // when
        // 이름을 없애고, save 이벤트를 날릴 때,
        viewModel.name.value = ""
        viewModel.saveCategory()

        // then
        // 토스트 메시지 확인 및 add/modify Category 메소드 동작 점검
        assertEquals(viewModel.toastMessage.takeValue(), R.string.input_category)
        verify(categoryRepository, never()).saveCategory(any())
        verify(categoryRepository, never()).modifyCategory(any())
    }

    @Test
    fun `새 카테고리 저장 시 테스트`() {
        // given
        // category Id를 null값으로 초기화
        viewModel.start(null)

        // when
        // 이름, 컬러 선택 후 save 이벤트 발생
        viewModel.name.value = CATEGORY_NAME
        viewModel.selectedColor.value = CATEGORY_COLOR
        viewModel.saveCategory()

        // then
        // 토스트 메시지 확인 및 add 메소드 동작 점검
        assertEquals(viewModel.toastMessage.takeValue(), R.string.category_added)
        assertEquals(viewModel.isSuccess.takeValue(), true)
        verify(categoryRepository).saveCategory(any())
    }

    @Test
    fun `기존 카테고리 수정을 위해 기존 카테고리 불러오기`() {
        // given
        viewModel.start(CATEGORY_ID)

        // callback 실행되지 않았으므로 loading 상태
        assertEquals(viewModel.isLoading.takeValue(), true)

        // when
        // getCategory의 Callback 을 잡아서 invoke,
        verify(categoryRepository).getCategory(eq(CATEGORY_ID), categoryCallbackCaptor.capture())
        categoryCallbackCaptor.firstValue.invoke(category)

        // then
        // 불러온 데이터 확인
        assertEquals(viewModel.isLoading.takeValue(), false)
        assertEquals(viewModel.name.value, CATEGORY_NAME)
        assertEquals(viewModel.selectedColor.value, CATEGORY_COLOR)
    }

    @Test
    fun `기존 카테고리 수정 시 이름이 없을 때 테스트`() {
        // given
        viewModel.start(CATEGORY_ID)

        verify(categoryRepository).getCategory(eq(CATEGORY_ID), categoryCallbackCaptor.capture())
        categoryCallbackCaptor.firstValue.invoke(category)

        // when
        // 이름을 없애고, save 이벤트 발생
        viewModel.name.value = ""
        viewModel.saveCategory()

        // then
        // 토스트 메시지 확인 및 repository 동작 유무 점검
        assertEquals(viewModel.toastMessage.takeValue(), R.string.input_category)
        verify(categoryRepository, never()).saveCategory(any())
        verify(categoryRepository, never()).modifyCategory(any())
    }

    @Test
    fun `기존 카테고리 수정 시 테스트`() {
        // given
        viewModel.start(CATEGORY_ID)

        verify(categoryRepository).getCategory(eq(CATEGORY_ID), categoryCallbackCaptor.capture())
        categoryCallbackCaptor.firstValue.invoke(category)

        // when
        // 이름, 컬러 변경 후 save 이벤트 발생
        viewModel.name.value = NEW_NAME
        viewModel.selectedColor.value = NEW_COLOR
        viewModel.saveCategory()

        // then
        // 토스트 메시지 및 modify 메소드 실행 여부 점검
        val modifiedCategory = category.apply {
            name = NEW_NAME
            colorCode = NEW_COLOR.name
        }
        assertEquals(viewModel.toastMessage.takeValue(), R.string.category_modified)
        assertEquals(viewModel.isSuccess.takeValue(), true)
        verify(categoryRepository).modifyCategory(eq(modifiedCategory))
    }

    companion object {
        const val CATEGORY_ID = "id"
        const val CATEGORY_NAME = "name"
        val CATEGORY_COLOR = Category.Color.ORANGE

        const val NEW_NAME = "new name"
        val NEW_COLOR = Category.Color.GREEN
    }
}
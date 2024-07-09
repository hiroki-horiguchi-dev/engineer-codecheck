package jp.co.yumemi.android.code_check.ui.search

import GithubResponse
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import jp.co.yumemi.android.code_check.data.FavoriteRepository
import jp.co.yumemi.android.code_check.data.GithubRepository
import jp.co.yumemi.android.code_check.network.model.Item
import jp.co.yumemi.android.code_check.network.model.Owner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class SearchFragmentViewModelTest {

    @MockK
    private lateinit var githubRepository: GithubRepository

    @MockK
    private lateinit var favoriteRepository: FavoriteRepository

    private lateinit var viewModel: SearchFragmentViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchFragmentViewModel(githubRepository, favoriteRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun 初期状態のテスト() {
        Assert.assertEquals(viewModel.items.value.size, 0)
        Assert.assertEquals(viewModel.errorMessage.value, "")
        Assert.assertEquals(viewModel.isAddFavoritePosition.value, -1)
        Assert.assertEquals(viewModel.isUnRemoveFavoritePosition.value, -1)
    }

    @Test
    fun 検索API成功() = runTest(testDispatcher) {
        val searchText = "test"
        val items = listOf(
            Item(
                name = "repo1",
                owner = Owner(avatarUrl = "https://example.com/avatar1"),
                language = "Kotlin",
                stargazersCount = 100,
                watchersCount = 50,
                forksCount = 30,
                openIssuesCount = 10
            ),
            Item(
                name = "repo2",
                owner = Owner(avatarUrl = "https://example.com/avatar2"),
                language = "Java",
                stargazersCount = 200,
                watchersCount = 150,
                forksCount = 100,
                openIssuesCount = 20
            )
        )
        val githubResponse =
            GithubResponse(totalCount = 2, incompleteResults = false, items = items)
        coEvery { githubRepository.fetchGithubRepository(searchText) } returns Response.success(
            githubResponse
        )
        every { favoriteRepository.getAllFavorites() } returns emptyList()

        viewModel.search(searchText)

        // TODO("稀に advanceUntilIdle が効かないので Thread.sleep で代替")
        delay(1000)
        val resultItems = viewModel.items.first()
        Assert.assertEquals(2, resultItems.size)
        Assert.assertEquals("repo1", resultItems[0].name)
        Assert.assertEquals("repo2", resultItems[1].name)
    }

    @Test
    fun 検索API失敗_403() = runTest(testDispatcher) {
        val searchText = "test"
        coEvery { githubRepository.fetchGithubRepository(searchText) } returns Response.error(
            403,
            mockk(relaxed = true)
        )
        viewModel.search(searchText)

        // TODO("advanceUntilIdle が時々効かないので Thread.sleep で代替するがちゃんと書き直ししたい")
        delay(1000)
        val resultMessage = viewModel.errorMessage.first()
        Assert.assertEquals("閲覧禁止です", resultMessage)
    }

    @Test
    fun 検索API失敗_404() = runTest(testDispatcher) {
        val searchText = "test"
        coEvery { githubRepository.fetchGithubRepository(searchText) } returns Response.error(
            404,
            mockk(relaxed = true)
        )
        viewModel.search(searchText)

        // TODO("advanceUntilIdle が時々効かないので Thread.sleep で代替するがちゃんと書き直ししたい")
        delay(1000)
        val resultMessage = viewModel.errorMessage.first()
        Assert.assertEquals("検索結果がありません", resultMessage)
    }

    @Test
    fun 検索API失敗_500() = runTest(testDispatcher) {
        val searchText = "test"
        coEvery { githubRepository.fetchGithubRepository(searchText) } returns Response.error(
            500,
            mockk(relaxed = true)
        )
        viewModel.search(searchText)

        // TODO("advanceUntilIdle が時々効かないので Thread.sleep で代替するがちゃんと書き直ししたい")
        delay(1000)
        val resultMessage = viewModel.errorMessage.first()
        Assert.assertEquals("サーバーエラーです", resultMessage)
    }

    @Test
    fun 検索API失敗_503() = runTest(testDispatcher) {
        val searchText = "test"
        coEvery { githubRepository.fetchGithubRepository(searchText) } returns Response.error(
            503,
            mockk(relaxed = true)
        )
        viewModel.search(searchText)

        // TODO("advanceUntilIdle が時々効かないので Thread.sleep で代替するがちゃんと書き直ししたい")
        delay(1000)
        val resultMessage = viewModel.errorMessage.first()
        Assert.assertEquals("メンテナンス中です", resultMessage)
    }

    @Test
    fun 検索API失敗_520() = runTest(testDispatcher) {
        val searchText = "test"
        coEvery { githubRepository.fetchGithubRepository(searchText) } returns Response.error(
            520,
            mockk(relaxed = true)
        )
        viewModel.search(searchText)

        // TODO("advanceUntilIdle が時々効かないので Thread.sleep で代替するがちゃんと書き直ししたい")
        delay(1000)
        val resultMessage = viewModel.errorMessage.first()
        Assert.assertEquals("不明なエラーが発生しました", resultMessage)
    }
}
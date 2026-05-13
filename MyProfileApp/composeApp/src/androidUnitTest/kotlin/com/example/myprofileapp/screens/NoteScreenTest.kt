package com.example.myprofileapp.screens

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import androidx.navigation.NavController
import com.example.myprofileapp.ai.GeminiService
import com.example.myprofileapp.data.ProfileRepository
import com.example.myprofileapp.db.NoteEntity
import com.example.myprofileapp.viewmodel.ProfileViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class NotesScreenTest {
    // Setup MockK untuk memalsukan dependencies
    private lateinit var mockNavController: NavController
    private lateinit var mockRepository: ProfileRepository
    private lateinit var mockGeminiService: GeminiService
    private lateinit var viewModel: ProfileViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        mockNavController = mockk<NavController>(relaxed = true)
        mockRepository = mockk<ProfileRepository>(relaxed = true)
        mockGeminiService = mockk<GeminiService>(relaxed = true)

        every { mockRepository.isDarkMode } returns flowOf(false)
        every { mockRepository.profileName } returns flowOf("User")
        every { mockRepository.profileBio } returns flowOf("Bio")
        every { mockRepository.getFavoriteNotes() } returns flowOf(emptyList())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        stopKoin()
    }

    // Test Memastikan pesan kosong muncul saat state Empty
    @Test
    fun testEmptyState_showsEmptyMessage() = runComposeUiTest {
        every { mockRepository.getNotes(any()) } returns flowOf(emptyList())
        viewModel = ProfileViewModel(mockRepository, mockGeminiService)

        setContent {
            NotesScreen(navController = mockNavController, viewModel = viewModel)
        }

        onNodeWithText("Belum ada catatan. Klik + untuk menambah.").assertIsDisplayed()
    }

    // Test Memastikan daftar catatan muncul saat state Success
    @Test
    fun testSuccessState_displaysNotes() = runComposeUiTest {
        val mockNotes = listOf(
            NoteEntity("1", "Tugas PAM", "Selesaikan UI Test", 0L, 0L),
            NoteEntity("2", "Ide Final Project", "Bikin aplikasi AI", 0L, 0L)
        )
        every { mockRepository.getNotes(any()) } returns flowOf(mockNotes)
        viewModel = ProfileViewModel(mockRepository, mockGeminiService)

        setContent {
            NotesScreen(navController = mockNavController, viewModel = viewModel)
        }

        onNodeWithText("Tugas PAM").assertIsDisplayed()
        onNodeWithText("Ide Final Project").assertIsDisplayed()
    }

    // Test Memastikan konten lain tidak muncul saat state Loading
    @Test
    fun testLoadingState_doesNotDisplayNotesOrEmptyMessage() = runComposeUiTest {
        val loadingFlow = MutableSharedFlow<List<NoteEntity>>()
        every { mockRepository.getNotes(any()) } returns loadingFlow
        viewModel = ProfileViewModel(mockRepository, mockGeminiService)

        setContent {
            NotesScreen(navController = mockNavController, viewModel = viewModel)
        }

        onNodeWithText("Belum ada catatan. Klik + untuk menambah.").assertDoesNotExist()
        onNodeWithText("Tugas PAM").assertDoesNotExist()
    }
}
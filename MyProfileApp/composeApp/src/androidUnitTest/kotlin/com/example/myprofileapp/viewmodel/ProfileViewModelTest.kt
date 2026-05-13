package com.example.myprofileapp.viewmodel

import app.cash.turbine.test
import com.example.myprofileapp.ai.GeminiService
import com.example.myprofileapp.data.ProfileRepository
import com.example.myprofileapp.db.NoteEntity
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {
    // Setup MockK untuk memalsukan dependencies
    private val mockRepository = mockk<ProfileRepository>(relaxed = true)
    private val mockGeminiService = mockk<GeminiService>()
    private lateinit var viewModel: ProfileViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        // Atur nilai kembalian default (stubbing) agar init ViewModel tidak error
        every { mockRepository.isDarkMode } returns flowOf(false)
        every { mockRepository.profileName } returns flowOf("Reyhan Oktavian Putra")
        every { mockRepository.profileBio } returns flowOf("Mahasiswa ITERA")
        every { mockRepository.getFavoriteNotes() } returns flowOf(emptyList())

        // Default behavior untuk getNotes
        val mockNote = NoteEntity("1", "Tugas PAM", "Belajar KMP", 0L, 12345L)
        every { mockRepository.getNotes(any()) } returns flowOf(listOf(mockNote))

        // Inisialisasi ViewModel dengan dependencies yang sudah di-mock
        viewModel = ProfileViewModel(mockRepository, mockGeminiService)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    //Flow test dengan Turbine
    @Test
    fun `notesUiState emits Loading then Success state`() = runTest {
        viewModel.notesUiState.test {
            assertTrue(awaitItem() is NotesUiState.Loading)

            val successState = awaitItem()
            assertTrue(successState is NotesUiState.Success)
            assertEquals(1, (successState as NotesUiState.Success).notes.size)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchQuery flow updates state correctly`() = runTest {
        viewModel.searchQuery.test {
            assertEquals("", awaitItem())

            viewModel.updateSearchQuery("KMP")
            assertEquals("KMP", awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    //Unit test dengan Mockk
    @Test
    fun `addNote calls saveNote on repository`() = runTest {
        val title = "Catatan Baru"
        val content = "Isi catatan"
        coEvery { mockRepository.saveNote(any(), any(), any(), any()) } just Runs

        viewModel.addNote(title, content)
        advanceUntilIdle()

        coVerify(exactly = 1) { mockRepository.saveNote(any(), title, content, false) }
    }

    @Test
    fun `deleteNote calls deleteNote on repository`() = runTest {
        val noteId = "123"
        coEvery { mockRepository.deleteNote(any()) } just Runs

        viewModel.deleteNote(noteId)
        advanceUntilIdle()

        coVerify(exactly = 1) { mockRepository.deleteNote(noteId) }
    }

    @Test
    fun `toggleFavorite calls toggleFavorite on repository`() = runTest {
        val noteId = "456"
        coEvery { mockRepository.toggleFavorite(any()) } just Runs

        viewModel.toggleFavorite(noteId)
        advanceUntilIdle()

        coVerify(exactly = 1) { mockRepository.toggleFavorite(noteId) }
    }

    @Test
    fun `generateTitleWithAi updates aiTitleState securely on success`() = runTest {
        val content = "Teks catatan yang panjang"
        val expectedTitle = "Judul dari Gemini"

        coEvery { mockGeminiService.generateTitle(any()) } returns Result.success(expectedTitle)

        // Panggil fungsi AI
        viewModel.generateTitleWithAi(content)
        advanceUntilIdle()

        //UI state ter-update sesuai respons AI
        val currentState = viewModel.aiTitleState.value
        assertFalse(currentState.isLoading)
        assertEquals(expectedTitle, currentState.suggestedTitle)
        assertNull(currentState.error)
    }
}
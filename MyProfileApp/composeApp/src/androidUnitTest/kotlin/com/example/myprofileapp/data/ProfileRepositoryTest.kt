package com.example.myprofileapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.myprofileapp.db.NoteDatabase
import com.example.myprofileapp.db.NoteEntityQueries
import io.mockk.*
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ProfileRepositoryTest {
    // Setup MockK untuk memalsukan dependencies
    private val mockDb = mockk<NoteDatabase>()
    private val mockQueries = mockk<NoteEntityQueries>()
    private val mockDataStore = mockk<DataStore<Preferences>>(relaxed = true)

    private lateinit var repository: ProfileRepository

    @BeforeTest
    fun setup() {
        every { mockDb.noteEntityQueries } returns mockQueries
        repository = ProfileRepository(mockDb, mockDataStore)
    }

    // Test Memastikan fungsi hapus memanggil query delete
    @Test
    fun `deleteNote calls queries deleteNote`() = runTest {
        val noteId = "123"
        every { mockQueries.deleteNote(any()) } just Runs

        repository.deleteNote(noteId)

        verify(exactly = 1) { mockQueries.deleteNote(noteId) }
    }

    // Test Memastikan fungsi toggle favorite memanggil query toggle
    @Test
    fun `toggleFavorite calls queries toggleFavorite`() = runTest {
        val noteId = "456"
        every { mockQueries.toggleFavorite(any()) } just Runs

        repository.toggleFavorite(noteId)

        verify(exactly = 1) { mockQueries.toggleFavorite(noteId) }
    }

    // Test Memastikan updateNote menyimpan urutan data yang benar ke query
    @Test
    fun `updateNote calls queries updateNote with correct mapped parameters`() = runTest {
        val id = "789"
        val title = "Judul Baru"
        val content = "Isi Baru"
        every { mockQueries.updateNote(any(), any(), any()) } just Runs

        repository.updateNote(id, title, content)

        verify(exactly = 1) { mockQueries.updateNote(title, content, id) }
    }

    // Test Memastikan saveNote menyimpan data sebagai BUKAN favorit (0L) secara default
    @Test
    fun `saveNote without isFav parameter sets favorite to 0L`() = runTest {
        val id = "101"
        val title = "Tugas KMP"
        val content = "Belajar Unit Test"
        every { mockQueries.insertNote(any(), any(), any(), any(), any()) } just Runs

        repository.saveNote(id, title, content)

        verify(exactly = 1) {
            mockQueries.insertNote(id, title, content, 0L, any())
        }
    }

    // Test Memastikan saveNote menyimpan data SEBAGAI favorit (1L) jika diset true
    @Test
    fun `saveNote with isFav true sets favorite to 1L`() = runTest {
        val id = "202"
        val title = "Tugas KMP"
        val content = "Selesai"
        every { mockQueries.insertNote(any(), any(), any(), any(), any()) } just Runs

        repository.saveNote(id, title, content, isFav = true)

        verify(exactly = 1) {
            mockQueries.insertNote(id, title, content, 1L, any())
        }
    }
}
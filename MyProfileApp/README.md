Nama: Reyhan Oktavian Putra <br>
NIM: 123140202 <br>
Tugas 10 <br>

# 🧪 Week 10: Dependency Injection & Testing (KMP)

Repositori ini berisi implementasi Tugas Praktikum Pertemuan 10 untuk mata kuliah Pengembangan Aplikasi Mobile (PAM). Fokus utama pada minggu ini adalah pengujian kualitas kode (*Testing*) dan penerapan arsitektur *Dependency Injection* menggunakan Koin pada proyek Kotlin Multiplatform (KMP).

## 💉 Koin Dependency Injection Setup
Koin DI telah diimplementasikan dengan memisahkan *dependencies* ke dalam **3 modul** utama agar memenuhi prinsip *Clean Architecture* dan *Loose Coupling*:
1. **`dataModule`**: Menyediakan *singleton* untuk `ProfileRepository` dan `NoteDatabase`.
2. **`networkModule`**: Menyediakan instance `HttpClient` (Ktor) dan `GeminiService` untuk integrasi AI.
3. **`viewModelModule`**: Menyediakan *factory* injection untuk `ProfileViewModel`.

## 📋 Daftar Test Cases

Proyek ini memiliki total **14 Test Cases** yang semuanya lulus (*Passed*) dengan rincian sebagai berikut:

### A. Repository Tests (`ProfileRepositoryTest.kt`)
Terdapat 5 *Unit Test Cases* untuk lapisan data yang memanfaatkan **MockK** untuk memalsukan (*mocking*) `NoteDatabase` dan `DataStore`:
1. ✅ `deleteNote calls queries deleteNote`: Memastikan query *delete* SQL dipanggil dengan ID yang tepat.
2. ✅ `toggleFavorite calls queries toggleFavorite`: Memastikan query *toggle* dipanggil saat status favorit diubah.
3. ✅ `updateNote calls queries updateNote with correct mapped parameters`: Memastikan pemetaan parameter judul, isi, dan ID dilakukan dengan urutan yang benar.
4. ✅ `saveNote without isFav parameter sets favorite to 0L`: Memastikan catatan baru disimpan sebagai non-favorit secara *default*.
5. ✅ `saveNote with isFav true sets favorite to 1L`: Memastikan catatan disimpan dengan status favorit jika parameter *boolean* diatur menjadi *true*.

### B. ViewModel Tests (`ProfileViewModelTest.kt`)
Terdapat 6 *Test Cases* untuk `ProfileViewModel` yang memadukan **MockK** untuk injeksi dependensi dan **Turbine** untuk menguji *StateFlow*:

**Flow Tests (Turbine):**
1. ✅ `notesUiState emits Loading then Success state`: Menguji urutan transisi aliran data dari status *Loading* menjadi *Success* saat memuat catatan.
2. ✅ `searchQuery flow updates state correctly`: Memastikan `StateFlow` dari kolom pencarian bereaksi dengan cepat saat pengguna mengetik.

**Unit Tests (MockK):**
3. ✅ `addNote calls saveNote on repository`: Memverifikasi interaksi ViewModel ke Repository saat menambah catatan.
4. ✅ `deleteNote calls deleteNote on repository`: Memverifikasi interaksi ViewModel ke Repository saat menghapus catatan.
5. ✅ `toggleFavorite calls toggleFavorite on repository`: Memverifikasi interaksi ViewModel ke Repository saat mengubah status favorit.

## 📊 Screenshot
Hasil Test: <br>
<img width="1190" height="340" alt="Screenshot 2026-05-13 191249" src="https://github.com/user-attachments/assets/19dcd35a-0a46-41f3-b952-2812d79651e3" />
<img width="1202" height="304" alt="Screenshot 2026-05-13 192208" src="https://github.com/user-attachments/assets/488cb3c6-6c64-49c2-ab93-3241e6e8236e" />
<img width="1215" height="297" alt="Screenshot 2026-05-13 195325" src="https://github.com/user-attachments/assets/684f56a2-ce39-4040-8144-3a81e2e9a866" />

Hasil Report: <br>
<img width="1224" height="521" alt="image" src="https://github.com/user-attachments/assets/279cfe86-bdf4-442f-95b4-4c2917499f7e" />




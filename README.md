Nama: Reyhan Oktavian Putra <br>
NIM: 123140202

Tugas 9 - Generate judul catatan menggunakan Gemini AI -> MyProfileApp <br>

## 🧠 Fitur AI: Auto Generate Title for Notes (Gemini API)

### 👨🏿‍🎓 Prompt Engineering
Untuk memastikan keluaran AI selalu rapi dan sesuai dengan desain UI aplikasi, fitur ini memanfaatkan *System Instruction* dengan parameter yang sangat spesifik (batasan kata dan format):
> *"Kamu adalah penulis judul yang singkat dan padat."*
> *"Tugasmu: Buatkan judul untuk catatan yang diberikan."*
> *"Aturan: Judul TIDAK BOLEH lebih dari 10 kata. Judul meringkas dari isi catatan dengan bahasa yang tidak kaku."*
> *"Return hasil jadi judul dan bukan list atau pilihan."*

### 👷🏿 Error Handling & Keselamatan
Integrasi API dibungkus menggunakan *Service Layer* khusus (`GeminiService`) dengan pendekatan `Result<T>` dan *Sealed Class* `AIError` untuk menangani *edge cases* secara elegan tanpa *force close*:
- **Rate Limiting (HTTP 429):** Mendeteksi batas limit *Free Tier* Gemini dan menampilkannya ke pengguna.
- **Safety Filter:** Memeriksa JSON `candidates` yang kosong apabila teks input diblokir oleh filter keamanan Google.
- **Network & Parsing:** Menangani `IOException` dan kegagalan `SerializationException`.

### 👩🏿‍🎨 UI / UX & State Management
- **Loading State:** Menampilkan `CircularProgressIndicator` interaktif dan teks *"AI sedang berpikir..."* saat request sedang berjalan, sekaligus menonaktifkan tombol (disabled) agar pengguna tidak melakukan *spam click*.
- **Error Feedback:** Jika API gagal, pesan error yang informatif akan muncul dalam teks berwarna merah di antarmuka `AddNoteScreen` / `EditNoteScreen`.
- Teks judul di UI akan langsung terisi secara otomatis (*reactive state*) begitu respons sukses diterima dari *ViewModel*.

### 👮🏿 Code Quality & Security
- **Clean Architecture:** Memisahkan *logic* jaringan dengan menggunakan Ktor Client dan diinjeksi via Koin DI.
- **Secure API Key:** *API Key* Google Gemini diisolasi secara aman menggunakan `local.properties` dan `BuildConfig` yang dipadukan dengan pola `expect/actual` dari Kotlin Multiplatform.

### 📸 Screenshot:
Tampilan awal: <br>
<img width="516" height="1121" alt="Screenshot 2026-05-03 191631" src="https://github.com/user-attachments/assets/1c2ba3cc-a0fd-448d-8488-db126294ad26" />
<br>
Tampilan loading: <br>
<img width="507" height="1116" alt="Screenshot 2026-05-03 191703" src="https://github.com/user-attachments/assets/64912c50-6349-4287-aa52-9a2366556b28" />
<br>
Tampilan hasil generate: <br>
<img width="503" height="1080" alt="Screenshot 2026-05-03 192130" src="https://github.com/user-attachments/assets/915a6121-0e89-4944-9d19-0c2c9c611c28" />
<br>
Tampilan Error: <br>
<img width="376" height="808" alt="image" src="https://github.com/user-attachments/assets/230dc455-e3f0-4cf0-a7cc-0fdda329608e" />






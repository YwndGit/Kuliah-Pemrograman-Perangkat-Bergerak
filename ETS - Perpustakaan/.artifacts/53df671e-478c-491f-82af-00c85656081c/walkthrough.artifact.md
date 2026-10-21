# Walkthrough - Aplikasi LibraryReg (Pendaftaran Anggota Perpustakaan Kampus)

Aplikasi Android **LibraryReg** telah selesai dikembangkan sepenuhnya sesuai dengan seluruh ketentuan tugas kuliah (ETS).

## Ringkasan Arsitektur & Teknologi
- **Bahasa**: Kotlin
- **UI Framework**: Jetpack Compose & Material Design 3
- **Navigasi**: Navigation Compose (5 Layar terintegrasi)
- **Arsitektur**: MVVM (Model-View-ViewModel) dengan `StateFlow` & Kotlin Coroutines
- **Penyimpanan Lokal**: Room Database dengan KSP
- **Warna Utama**: Biru Tua (`#1E3A8A`), Hijau Sukses (`#16A34A`), Merah Hapus (`#DC2626`), sudut *rounded* 8-12dp.

---

## 5 Layar Utama & Cara Kerjanya

### 1. Layar Beranda (`BerandaScreen.kt`)
- Menampilkan logo perpustakaan, judul **LibraryReg**, dan subjudul.
- Menyediakan 4 tombol menu utama yang terhubung ke alur sistem:
  - **Pendaftaran Anggota** ➔ Membuka Formulir Pendaftaran.
  - **Daftar Anggota** ➔ Membuka Daftar Anggota.
  - **Pencarian Data** ➔ Membuka Daftar Anggota dengan fitur pencarian langsung.
  - **Kelola Data** ➔ Membuka Daftar Anggota.

### 2. Formulir Pendaftaran (`FormulirScreen.kt`)
- Form lengkap mencakup: Nama Lengkap, NIM/NIP, Program Studi/Unit (Dropdown), Email, Nomor Telepon, Alamat (Multiline), dan Jenis Keanggotaan (Dropdown: Mahasiswa, Dosen, Staf).
- **Validasi Wajib**:
  - Semua kolom wajib diisi.
  - Format email harus valid (`android.util.Patterns`).
  - NIM/NIP hanya boleh berisi angka (`\d+`).
  - Cek duplikat NIM/NIP di database Room (NIM/NIP sebagai *Primary Key*). Pesan error muncul di bawah masing-masing field.
- Tombol **Daftar** menyimpan data ke Room secara asynchronous, menampilkan pesan sukses berwarna hijau (`#16A34A`), lalu otomatis kembali ke Daftar Anggota.

### 3. Daftar Anggota (`DaftarAnggotaScreen.kt`)
- **Pencarian Real-Time**: Kolom pencarian di bagian atas untuk menyaring data berdasarkan Nama atau NIM/NIP secara instan menggunakan `StateFlow` & Room `Flow`.
- **LazyColumn**: Menampilkan daftar anggota dalam bentuk kartu (*Card*) dengan sudut *rounded*.
- **Aksi per Item**:
  - Ikon **Lihat** (Mata) ➔ Membuka Layar Detail.
  - Ikon **Edit** (Pensil) ➔ Membuka Layar Edit.
  - Ikon **Hapus** (Tempat Sampah Merah) ➔ Memunculkan dialog konfirmasi.
- **Tampilan Kosong**: Menampilkan pesan `"Belum ada data anggota"` jika belum ada data.
- **FAB (`+`)**: Tombol aksi mengambang untuk menambah anggota baru.

### 4. Detail Anggota (`DetailAnggotaScreen.kt`)
- Menampilkan informasi lengkap data anggota terpilih.
- Tombol **Edit** dan **Hapus**.
- **Dialog Konfirmasi Hapus**: Memunculkan pop-up `"Yakin ingin menghapus data ini?"` dengan tombol aksi konfirmasi berwarna merah (`#DC2626`). Setelah dihapus, kembali ke Daftar Anggota.

### 5. Edit Anggota (`EditAnggotaScreen.kt`)
- Memuat form yang sudah terisi data lama anggota.
- NIM/NIP dibuat *Read-Only* / *Disabled* karena bertindak sebagai *Primary Key* yang tidak boleh diubah.
- Validasi data serupa dengan form pendaftaran.
- Tombol **Simpan Perubahan** memperbarui data di Room dan kembali ke layar sebelumnya.

---

## Verifikasi & Pengujian
- Proyek berhasil di-build tanpa error (`BUILD SUCCESSFUL`).
- Semua teks UI menggunakan Bahasa Indonesia.
- Konsisten dengan palet warna dan prinsip desain Material 3 yang diminta.

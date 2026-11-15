# UTS Struktur Data – Putusan Narkotika (Kelompok 01)

## Identitas Kelompok

| Nama                          | NIM             | Kelas |
|-------------------------------|-----------------|-------|
| Aurellia Mahanesha Yuri Putri | 202510370110018 | B     |
| Ghina Roudhotul Jannah        | 202510370110029 | B     |

**Dosen Pengampu:** Galih W. Wicaksono  
**Mata Kuliah:** Struktur Data  
**Semester:** Ganjil 2026/2027  
**Program Studi:** Informatika  
**Universitas:** Universitas Muhammadiyah Malang

---

## Deskripsi Proyek

Proyek ini merupakan implementasi struktur data dalam pengelolaan dan pengolahan dataset putusan perkara narkotika. Data yang digunakan berasal dari dokumen putusan **Pengadilan Negeri Surakarta**.

Aplikasi dikembangkan menggunakan bahasa pemrograman Java dengan menerapkan struktur data Singly Linked List sebagai struktur utama dan Stack berbasis array sebagai struktur pendukung untuk pengelolaan riwayat aktivitas atau audit.

Melalui proyek ini, pengguna dapat melakukan berbagai operasi pengelolaan data, seperti menambahkan, menghapus, mencari, menampilkan, memfilter, mengurutkan, dan mengekspor data putusan.

## Struktur Data yang Digunakan

Proyek ini menggunakan dua struktur data yang diimplementasikan secara mandiri.

### 1. Struktur Utama – Singly Linked List Generik (`SinglyLinkedList<T>`)

Singly Linked List digunakan sebagai struktur data utama untuk menyimpan dan mengelola kumpulan data putusan narkotika.

Karakteristik dan operasi:
- Menggunakan node yang saling terhubung melalui referensi ke node berikutnya.
- Mendukung penambahan elemen pada bagian awal dan akhir.
- Mendukung penyisipan elemen pada posisi tertentu.
- Mendukung penghapusan elemen.
- Mendukung pencarian dan pengambilan data.
- Digunakan dalam proses penampilan, filter, agregasi, dan pengurutan data.

### 2. Struktur Sekunder – Stack Berbasis Array Generik (`StackArray<T>`)

Stack digunakan sebagai struktur data pendukung untuk menyimpan riwayat aktivitas atau audit selama aplikasi berjalan.

Karakteristik dan operasi:
- Menggunakan prinsip LIFO (*Last In, First Out*).
- Diimplementasikan menggunakan array.
- Mendukung operasi `push`, `pop`, dan `peek`.
- Mendukung pencarian elemen melalui `searchPreserve` dengan tetap mempertahankan isi Stack.

### Ketentuan Implementasi

- Struktur data inti dibuat sendiri menggunakan Java.
- Tidak menggunakan koleksi bawaan Java sebagai pengganti implementasi Singly Linked List dan Stack.
- Implementasi menggunakan konsep generik (`<T>`).
- GUI berbasis Java Swing merupakan fitur tambahan.
- Aplikasi konsol tetap menjadi jalur utama untuk demonstrasi dan pengujian proyek.

## Dataset

Dataset yang digunakan dalam proyek ini merupakan kumpulan **50 data putusan perkara narkotika** yang bersumber dari dokumen putusan **Pengadilan Negeri Surakarta**.

### Ketentuan Dataset

- File dataset utama: `data/putusan-50.csv`.
- Jumlah data: 50 putusan perkara narkotika.
- Sumber data: Pengadilan Negeri Surakarta.
- File CSV menggunakan 16 kolom sesuai definisi `CsvPutusan.HEADER`.
- Identitas terdakwa disamarkan menggunakan kode T001–T050.
- Data yang tidak ditemukan dalam dokumen sumber diisi dengan nilai `0` atau dikosongkan sesuai tipe datanya.
- Atribut `beratBarangBuktiGram` berisi jumlah berat netto narkotika dalam satuan gram.
- Jika dokumen hanya mencantumkan berat bruto atau tidak mencantumkan berat netto, nilai `beratBarangBuktiGram` diisi `0`.

### Proses Pengolahan Dataset

Data putusan diperoleh dari dokumen sumber, kemudian dilakukan proses ekstraksi untuk mengambil informasi yang dibutuhkan sesuai atribut dataset.

Tahapan pengolahan data meliputi:
1. Mengumpulkan dokumen putusan perkara narkotika dari Pengadilan Negeri Surakarta.
2. Mengekstraksi informasi yang diperlukan dari setiap dokumen putusan.
3. Menyamarkan identitas terdakwa menggunakan kode T001–T050.
4. Menyesuaikan data hasil ekstraksi dengan struktur 16 kolom CSV.
5. Memeriksa kembali kesesuaian data dengan dokumen sumber.
6. Menyimpan data yang telah diperiksa ke dalam file `data/putusan-50.csv`.

## Teknologi yang Digunakan

| Teknologi | Kegunaan |
|---|---|
| Java | Bahasa pemrograman utama |
| Maven | Pengelolaan dependensi dan proses build |
| IntelliJ IDEA | Lingkungan pengembangan aplikasi |
| CSV | Format penyimpanan dataset putusan |
| Git | Pengelolaan versi kode program |
| GitHub | Penyimpanan repository dan kolaborasi kelompok |
| Java Swing | Pengembangan antarmuka grafis tambahan |

## Build dan Pengujian

Untuk memastikan seluruh kode program dapat dikompilasi dan pengujian berjalan, buka terminal pada direktori utama proyek.

Jalankan perintah berikut:

```bash
mvn clean compile
mvn test
```

Keterangan:
- `mvn clean compile`: membersihkan hasil kompilasi sebelumnya dan mengompilasi seluruh kode program.
- `mvn test`: menjalankan pengujian otomatis yang tersedia pada proyek.

## Menjalankan Aplikasi Konsol

Aplikasi konsol dapat dijalankan menggunakan perintah berikut:

```bash
mvn compile exec:java -Dexec.args="data/putusan-50.csv"
```

### Alternatif Menjalankan Melalui IntelliJ IDEA

1. Buka proyek menggunakan IntelliJ IDEA.
2. Cari class `uts.app.Main`.
3. Buka konfigurasi Run.
4. Pada bagian **Program arguments**, masukkan:

```text
data/putusan-50.csv
```

5. Klik Run untuk menjalankan aplikasi.

## Menjalankan GUI Tambahan

Aplikasi juga menyediakan antarmuka grafis menggunakan Java Swing.

Langkah menjalankan GUI:

1. Buka proyek di IntelliJ IDEA.
2. Cari class `uts.gui.GuiMain`.
3. Klik kanan pada class tersebut.
4. Pilih **Run 'GuiMain.main()'**.
5. Tunggu hingga jendela aplikasi muncul.

GUI merupakan fitur tambahan, sedangkan aplikasi konsol tetap menjadi jalur utama demonstrasi proyek UTS.

## Menu Aplikasi

Aplikasi menyediakan menu untuk mengelola dan mengolah data putusan narkotika.

| No. | Menu | Fungsi |
|---|---|---|
| 1 | Impor | Memasukkan data putusan dari file CSV ke dalam aplikasi |
| 2 | Tampil | Menampilkan data putusan yang tersimpan |
| 3 | Tambah | Menambahkan data putusan baru |
| 4 | Hapus | Menghapus data putusan |
| 6 | Cari | Mencari data putusan berdasarkan kriteria tertentu |
| 7 | Filter | Menampilkan data berdasarkan kondisi atau kriteria tertentu |
| 8 | Agregasi | Melakukan perhitungan dan ringkasan terhadap data putusan |
| 9 | Urutan Vonis | Menampilkan data berdasarkan urutan vonis |
| 10 | Riwayat Audit | Menampilkan riwayat aktivitas yang tersimpan dalam Stack |
| 11 | Record Ditolak | Menampilkan data yang gagal diproses atau ditolak |
| 12 | Ekspor | Mengekspor data ke dalam file CSV |
| 13 | Demo Skenario UTS | Menjalankan demonstrasi skenario fitur aplikasi |

## Analisis Kompleksitas

Analisis kompleksitas digunakan untuk menjelaskan efisiensi setiap operasi pada struktur data yang diimplementasikan.

### Kompleksitas Singly Linked List

| Operasi | Kompleksitas | Keterangan |
|---|---|---|
| `addFirst` | O(1) | Menambahkan node pada bagian awal |
| `addLast` | O(1) | Menambahkan node pada bagian akhir dengan referensi tail |
| `get` | O(n) | Mengakses elemen berdasarkan indeks |
| `insert` | O(n) | Menyisipkan elemen pada posisi tertentu |
| `remove` | O(n) | Menghapus elemen berdasarkan posisi atau kriteria |
| `search` | O(n) | Mencari elemen dengan menelusuri node |
| `filter` | O(n) | Memeriksa seluruh elemen berdasarkan kondisi |
| `agregasi` | O(n) | Menghitung ringkasan dari seluruh elemen |
| `ekspor` | O(n) | Memproses seluruh elemen untuk disimpan |
| `insertSorted` berulang | O(n²) | Penyisipan berulang dalam kondisi terburuk |

### Kompleksitas Stack Berbasis Array

| Operasi | Kompleksitas | Keterangan |
|---|---|---|
| `push` | O(1) amortized | Menambahkan elemen ke Stack |
| `pop` | O(1) | Mengambil dan menghapus elemen teratas |
| `peek` | O(1) | Melihat elemen teratas tanpa menghapusnya |
| `searchPreserve` | O(n) | Mencari elemen dengan tetap mempertahankan isi Stack |

## Dokumentasi Proyek

Dokumentasi pendukung proyek tersedia pada direktori dan file berikut:

| Dokumentasi | Keterangan |
|---|---|
| `docs/adr` | Architecture Decision Records, berisi catatan keputusan desain dan alasan pemilihan implementasi struktur data |
| `docs/trace` | Dokumentasi keterlacakan data putusan terhadap dokumen sumber |
| `docs/demo` | Dokumentasi skenario demonstrasi dan pengujian fitur aplikasi |
| `CONTRIBUTIONS.md` | Berisi pembagian tugas dan kontribusi masing-masing anggota kelompok |
| `AI_USAGE.md` | Berisi dokumentasi penggunaan AI selama proses pengerjaan proyek |

## Tujuan Proyek

Proyek ini bertujuan untuk menerapkan konsep struktur data yang telah dipelajari dalam mata kuliah Struktur Data melalui studi kasus pengelolaan data putusan perkara narkotika.

Selain itu, proyek ini bertujuan untuk:
- Memahami implementasi Singly Linked List dan Stack menggunakan Java.
- Menerapkan konsep generik dalam pembuatan struktur data.
- Mengimplementasikan operasi pengelolaan data secara mandiri.
- Memahami analisis kompleksitas algoritma.
- Mengembangkan aplikasi yang mampu melakukan pengolahan dataset secara terstruktur.


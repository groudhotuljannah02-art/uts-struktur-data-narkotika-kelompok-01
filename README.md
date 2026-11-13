# UTS Struktur Data - Putusan Narkotika (Kelompok XX)

## Identitas Kelompok
| Nama | NIM | Kelas |
|---|---|---|
| Anggota A | isi | isi |
| Anggota B | isi | isi |

Dosen: Galih W. Wicaksono. Mata kuliah Struktur Data, Semester Ganjil 2026/2027.

## Struktur pilihan
- Struktur utama: Singly Linked List generik `SinglyLinkedList<T>`.
- Struktur sekunder: Stack berbasis array generik `StackArray<T>` untuk audit.
- Struktur inti dibuat sendiri, tidak menggunakan koleksi bawaan Java sebagai pengganti.
- GUI Swing adalah tambahan; aplikasi konsol tetap menjadi jalur demo utama.

## Dataset
Simpan data terverifikasi sebagai `data/putusan-50.csv` dengan 50 putusan dari 50 Pengadilan Negeri berbeda.
Header CSV harus 16 kolom sesuai `CsvPutusan.HEADER`. Nama terdakwa harus disamarkan menjadi T001-T050.
Nilai yang tidak ditemukan diisi 0 atau kosong sesuai tipe, bukan diperkirakan.
Aturan berat: `beratBarangBuktiGram` adalah jumlah berat netto narkotika dalam gram; jika sumber hanya memberi bruto atau tidak memberi berat netto, isi 0. Sesuaikan aturan ini dengan proses ekstraksi yang benar-benar digunakan kelompok.

## Build dan test
```bash
mvn clean compile
mvn test
```

## Menjalankan aplikasi konsol
```bash
mvn compile exec:java -Dexec.args="data/putusan-50.csv"
```
Atau jalankan `uts.app.Main` di IntelliJ dengan Program arguments `data/putusan-50.csv`.

## Menjalankan GUI tambahan
Jalankan class `uts.gui.GuiMain` dari IntelliJ.

## Menu
1 impor, 2 tampil, 3 tambah, 4 hapus, 6 cari, 7 filter, 8 agregasi, 9 urutan vonis,
10 riwayat audit, 11 record ditolak, 12 ekspor, 13 demo skenario UTS.

## Kompleksitas
| Operasi | Kompleksitas |
|---|---|
| addFirst, addLast | O(1) |
| get, insert, remove, search | O(n) |
| filter, agregasi, ekspor | O(n) |
| insertSorted berulang | O(n²) terburuk |
| Stack push/pop/peek | O(1) amortized |
| Stack searchPreserve | O(n) |

## Dokumentasi
Lihat `docs/adr`, `docs/trace`, `docs/demo`, `CONTRIBUTIONS.md`, dan `AI_USAGE.md`.

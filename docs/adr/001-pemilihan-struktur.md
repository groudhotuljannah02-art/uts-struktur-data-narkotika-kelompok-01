# ADR-001: Pemilihan struktur data

- Status: diterima.
- anggal: 2026-11-14

## Konteks
Aplikasi menyimpan 50 putusan dan menjalankan import, tampil, tambahan/hapus, cari, filter, agregasi, urutan, audit, dan ekspor. Struktur utama harus dibuat dari nol dan generik.

## Keputusan
Struktur utama adalah `SinglyLinkedList` Putusan dengan atribut `head`, `tail`, dan `size`.
Struktur sekunder adalah `StackArray` String berbasis array dinamis untuk menyimpan riwayat audit.

## Alasan
- Arsip sering ditambah dan dihapus pada bagian posisi. Pada `linked list` tidak ada pergeseran elemen; hanya penyesuaian pointer. Operasi addLast memiliki kompleksitas O(1) karena menyimpan pointer tail.
- Array dinamis cukup karena akses stack dilakukan melalui bagian puncak.
- Riwayat audit bersifat terbaru dahulu sehingga prinsip LIFO pada stack sesuai.

## Konsekuensi (n = jumlah elemen)
| Operasi | List utama | Stack audit |
|---|---|---|
| addFirst / addLast / push | O(1) | Amortized O(1) |
| insert(pos) | O(n) | - |
| get(pos) | O(n) | O(1) |
| remove(pos) | O(1) di awal, O(n) lainnya | O(1) hanya di puncak |
| search / contains | O(n) | O(n) dengan searchPreserve |

## Pemetaan ke kontak StrukturData T (Stack)
| Kontrak | Implementasi Stack | Alasan |
|---|---|---|
| add | push | Nama operasi Stack |
| get(i) | elemen indeks dari dasar | Tidak mengubah isi |
| remove(i) | hanya i = size-1 (pop) | Menghapus bagian tengah merusak LIFO |
| contains | telusur array tanpa mengubah Stack | Menjaga isi Stack |

## Invariant
- List: `size` sama dengan jumlah node; `head` dan `tail` bernilai null saat kosong; `tail.next` bernilai null; `tail` menunjuk node terakhir.
- Stack kapasitas minimal 4; 0 <= size <= kapasitas; slot di atas puncak bernilai null.
- Keduanya di periksa oleh validateInvariant() pada pengujian.

## Konsekuensi
Akses acak pada linked list membutuhkan O(n), tetapi dapat diterima untuk 50 record. Implementasi struktur data tidak menggunakan koleksi java.until.
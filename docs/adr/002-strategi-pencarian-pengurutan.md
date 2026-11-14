# ADR-002: Strategi pencarian dan pengurutan

- Status: diterima.
- Tanggal: 2026-11-14
## Pencarian
- Linear search pada linked list (`SinglyLinkedList.search`) digunakan untuk mencari `caseId` dan `nomorPerkara`.
- Data tidak terurut dan linked list tidak mendukung binary search karena tidak memiliki akses acak O(1).
- Setiap pencarian melaporkan jumlah langkah berupa jumlah node yang dikunjungi. Pencarian terbaik membutuhkan 1 langkah, sedangkan terburuk membutuhkan n langkah.
- Pencarian riwayat audit menggunakan `searchPreserve()`. Elemen dipindahkan sementara ke stack bantu, kemudian dikembalikan menggunakan `restore()` sehingga urutan akhir tetap sama. Kompleksitas O(n) waktu dan O(n) ruang bantu.

## Filter dan agregasi
- Filter menggunakan satu kali penelusuran O(n) dengan `forEach`. Hasil dikumpulkan ke linked list baru sehingga sumber tidak diubah.
- Agregasi menghitung jumlah perkara, rata-rata vonis, total denda, dan distribusi jenis narkotika.
- Penghitungan distribusi menggunakan list sementara dan pencarian jenis menggunakan `search()`. Kompleksitas terburuk dapat mencapai O(n²), bergantung pada jumlah jenis yang berbeda.

## Pengurutan (bedasarkan vonis)
- Insertion sort stabil ke list baru (`PengolahData.urutkanVonis`): O(n²) waktu dan O(n) ruang tambahan.
- Dipilih karena jumlah data n = 50, mudah dibuktikan benar, stabil (perkara dengan vonis sama tetap berurutan sesuai arsip), dan list utama tidak berubah sehingga invariant terjaga.
- Alternatif yang ditolak: merge sort pada linked list (O(n log n)), karena lebih rumit dan belum diperlukan pada n = 50.

## Konsekuensi
Strategi ini cocok untuk pengelola 50 record. Apabila jumlah data bertambah besar,strategi pengurutan dan agregasi dapat dievaluasi kembali tanpa mengubah tujuan antarmuka pengelola data.
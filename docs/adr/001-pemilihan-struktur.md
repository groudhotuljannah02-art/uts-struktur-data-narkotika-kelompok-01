# ADR-001: Pemilihan struktur data

Status: diterima.

## Konteks
Aplikasi mengelola arsip 50 objek Putusan. Tugas mewajibkan struktur utama dan struktur sekunder dibuat sendiri.

## Keputusan
Struktur utama adalah Singly Linked List generik. Struktur sekunder adalah Stack berbasis array generik untuk riwayat audit.

## Alasan
- Penambahan di akhir O(1) karena menyimpan pointer tail.
- Penghapusan dan penyisipan pada posisi tertentu dilakukan dengan mengubah pointer node.
- Stack cocok untuk riwayat audit dengan prinsip LIFO.

## Konsekuensi
- Akses berdasarkan indeks dan pencarian linked list O(n).
- Invariant head, tail, size, dan tail.next harus dijaga.
- Stack hanya menghapus elemen teratas agar prinsip LIFO tidak rusak.

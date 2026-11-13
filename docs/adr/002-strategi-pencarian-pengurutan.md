# ADR-002: Strategi pencarian dan pengurutan

Status: diterima.

## Pencarian
SinglyLinkedList menelusuri node dari head hingga ditemukan caseId/nomorPerkara atau mencapai akhir. Jumlah node yang dikunjungi disimpan pada getLangkah(). Kompleksitas O(n).

## Pengurutan
Tampilan terurut dibangun ke linked list hasil baru menggunakan insertSorted. Arsip asli tidak dimutasi. Kompleksitas terburuk O(n^2), dengan urutan stabil untuk nilai vonis yang sama.

## Alasan
Pendekatan ini mempertahankan invariant struktur utama dan membuat biaya pencarian dapat didemonstrasikan.

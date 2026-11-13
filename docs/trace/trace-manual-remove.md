# Trace manual penghapusan Singly Linked List

Sebelum: `head -> [A] -> [B] -> [C] -> null`, `tail=[C]`, `size=3`.

## remove(0)
Node A dihapus. Head berpindah ke B. Tail tetap C. Hasil: `head -> [B] -> [C] -> null`, `tail=[C]`, `size=2`.

## remove(1) pada list A-B-C
Node sebelum target adalah A, target B. Pointer `A.next` diubah menjadi C. Hasil: `head -> [A] -> [C] -> null`, `tail=[C]`, `size=2`.

## remove(2) pada list A-B-C
Node sebelum target adalah B, target C. Pointer `B.next` menjadi null dan tail mundur ke B. Hasil: `head -> [A] -> [B] -> null`, `tail=[B]`, `size=2`.

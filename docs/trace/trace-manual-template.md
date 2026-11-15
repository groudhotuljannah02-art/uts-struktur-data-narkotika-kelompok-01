# TRACE MANUAL OPERASI REMOVE SINGLY LINKED LIST

## 1. Kondisi Awal

Struktur data memiliki tiga node:

```text
head -> [A] -> [B] -> [C] -> null
tail = [C]
size = 3
```

## 2. Operasi remove(1)

**Kondisi sebelum:**

```text
head -> [A] -> [B] -> [C] -> null
tail = [C]
size = 3
```

Langkah penelusuran:
1. `nodeAt(0)` menghasilkan node A.
2. Target yang akan dihapus adalah node B.
3. Hubungkan `A.next = C`.
4. Tail tidak berubah.
5. Size berubah dari 3 menjadi 2.

**Kondisi sesudah:**

```text
head -> [A] -> [C] -> null
tail = [C]
size = 2
```

## 3. Operasi remove(2)

**Kondisi sebelum:**

```text
head -> [A] -> [B] -> [C] -> null
tail = [C]
size = 3
```

Langkah penelusuran:
1. Node sebelum target adalah B.
2. Target yang dihapus adalah C.
3. Hubungkan `B.next = null`.
4. Tail berpindah dari C ke B.
5. Size berubah dari 3 menjadi 2.

**Kondisi sesudah:**

```text
head -> [A] -> [B] -> null
tail = [B]
size = 2
```

## 4. Operasi remove(0)

**Kondisi sebelum:**

```text
head -> [A] -> [B] -> [C] -> null
tail = [C]
size = 3
```

Langkah penelusuran:
1. Target yang dihapus adalah node A.
2. Head berpindah ke node B.
3. Tail tidak berubah.
4. Size berubah dari 3 menjadi 2.

**Kondisi sesudah:**

```text
head -> [B] -> [C] -> null
tail = [C]
size = 2
```

## 5. Kesimpulan

Operasi remove menghapus node berdasarkan indeks. Penghapusan node tengah mengubah pointer node sebelumnya, penghapusan tail memindahkan tail ke node sebelumnya, sedangkan penghapusan head memindahkan head ke node berikutnya. Setiap operasi mengurangi ukuran list sebanyak satu.
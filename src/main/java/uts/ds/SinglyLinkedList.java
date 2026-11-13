package uts.ds;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SinglyLinkedList<T> implements StrukturData<T> {

    private static final class Node<T> {
        T nilai;
        Node<T> next;

        Node(T nilai) {
            this.nilai = nilai;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    private long langkahTerakhir;
    private long totalMutasi;

    public void addFirst(T value) {
        Node<T> baru = new Node<>(value);
        baru.next = head;
        head = baru;
        if (tail == null) {
            tail = baru;
        }
        size++;
        totalMutasi++;
    }

    public void addLast(T value) {
        Node<T> baru = new Node<>(value);
        if (tail == null) {
            head = baru;
        } else {
            tail.next = baru;
        }
        tail = baru;
        size++;
        totalMutasi++;
    }

    @Override
    public void add(T value) {
        addLast(value);
    }

    public void insert(int position, T value) {
        if (position < 0 || position > size) {
            throw new IndexOutOfBoundsException("Posisi sisip tidak valid: " + position + ", size=" + size);
        }
        if (position == 0) {
            addFirst(value);
        } else if (position == size) {
            addLast(value);
        } else {
            Node<T> sebelum = nodeAt(position - 1);
            Node<T> baru = new Node<>(value);
            baru.next = sebelum.next;
            sebelum.next = baru;
            size++;
            totalMutasi++;
        }
    }

    @Override
    public T get(int position) {
        cekPosisi(position);
        return nodeAt(position).nilai;
    }

    public T peekFirst() {
        return head == null ? null : head.nilai;
    }

    public T peekLast() {
        return tail == null ? null : tail.nilai;
    }

    @Override
    public T remove(int position) {
        cekPosisi(position);
        Node<T> dihapus;
        if (position == 0) {
            dihapus = head;
            head = head.next;
            if (head == null) {
                tail = null;
            }
        } else {
            Node<T> sebelum = nodeAt(position - 1);
            dihapus = sebelum.next;
            sebelum.next = dihapus.next;
            if (dihapus == tail) {
                tail = sebelum;
            }
        }
        dihapus.next = null;
        size--;
        totalMutasi++;
        return dihapus.nilai;
    }

    public int search(Predicate<T> kondisi) {
        long langkah = 0;
        int indeks = 0;
        Node<T> cur = head;
        while (cur != null) {
            langkah++;
            if (kondisi.test(cur.nilai)) {
                langkahTerakhir = langkah;
                return indeks;
            }
            cur = cur.next;
            indeks++;
        }
        langkahTerakhir = langkah;
        return -1;
    }

    public int indexOf(T value) {
        return search(x -> Objects.equals(x, value));
    }

    @Override
    public boolean contains(T value) {
        return indexOf(value) >= 0;
    }

    public long getLangkahTerakhir() {
        return langkahTerakhir;
    }

    public long getTotalMutasi() {
        return totalMutasi;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
        totalMutasi++;
    }

    public void forEach(Consumer<T> aksi) {
        Node<T> cur = head;
        while (cur != null) {
            aksi.accept(cur.nilai);
            cur = cur.next;
        }
    }

    public void validateInvariant() {
        if (size < 0) {
            throw new IllegalStateException("size negatif");
        }
        if (size == 0) {
            if (head != null || tail != null) {
                throw new IllegalStateException("List kosong tetapi head/tail tidak null");
            }
            return;
        }
        if (head == null || tail == null) {
            throw new IllegalStateException("List berisi tetapi head/tail null");
        }
        int hitung = 0;
        Node<T> cur = head;
        Node<T> terakhir = null;
        while (cur != null) {
            hitung++;
            terakhir = cur;
            cur = cur.next;
            if (hitung > size) {
                throw new IllegalStateException("Jumlah node melebihi size (kemungkinan siklus)");
            }
        }
        if (hitung != size) {
            throw new IllegalStateException("Jumlah node " + hitung + " != size " + size);
        }
        if (terakhir != tail) {
            throw new IllegalStateException("tail tidak menunjuk node terakhir");
        }
        if (tail.next != null) {
            throw new IllegalStateException("tail.next harus null");
        }
    }

    private void cekPosisi(int position) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException("Posisi tidak valid: " + position + ", size=" + size);
        }
    }

    private Node<T> nodeAt(int position) {
        Node<T> cur = head;
        for (int i = 0; i < position; i++) {
            cur = cur.next;
        }
        return cur;
    }
}

package uts.ds;

import java.util.function.Predicate;

public class StackArray<T> implements StrukturData<T> {

    public static final int KAPASITAS_MINIMAL = 4;

    private Object[] data = new Object[KAPASITAS_MINIMAL];
    private int size;
    private long langkahTerakhir;

    public void push(T value) {
        if (size == data.length) {
            resize(data.length * 2);
        }
        data[size++] = value;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (size == 0) {
            throw new IllegalStateException("Stack kosong");
        }
        T nilai = (T) data[--size];
        data[size] = null;
        if (data.length > KAPASITAS_MINIMAL && size <= data.length / 4) {
            resize(Math.max(KAPASITAS_MINIMAL, data.length / 2));
        }
        return nilai;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (size == 0) {
            throw new IllegalStateException("Stack kosong");
        }
        return (T) data[size - 1];
    }

    /**
     * Pencarian non-destruktif: elemen dipindah ke stack bantu lalu dikembalikan (restore).
     * @return posisi dari puncak (0 = puncak) atau -1 jika tidak ada.
     */
    public int searchPreserve(Predicate<T> kondisi) {
        StackArray<T> bantu = new StackArray<>();
        int ketemu = -1;
        long langkah = 0;
        while (!isEmpty()) {
            T x = pop();
            bantu.push(x);
            langkah++;
            if (kondisi.test(x)) {
                ketemu = (int) (langkah - 1);
                break;
            }
        }
        restore(bantu);
        langkahTerakhir = langkah;
        return ketemu;
    }

    /** Mengembalikan semua elemen stack bantu ke stack ini sehingga urutan awal pulih. */
    public void restore(StackArray<T> bantu) {
        while (!bantu.isEmpty()) {
            push(bantu.pop());
        }
    }

    public long getLangkahTerakhir() {
        return langkahTerakhir;
    }

    @Override
    public void add(T value) {
        push(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int position) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException("Posisi tidak valid: " + position + ", size=" + size);
        }
        return (T) data[position];
    }

    @Override
    public T remove(int position) {
        if (position != size - 1) {
            throw new IllegalArgumentException("Stack hanya boleh menghapus elemen teratas (posisi " + (size - 1) + ")");
        }
        return pop();
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
    public boolean contains(T value) {
        for (int i = 0; i < size; i++) {
            if (data[i] == null ? value == null : data[i].equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        data = new Object[KAPASITAS_MINIMAL];
        size = 0;
    }

    public int kapasitas() {
        return data.length;
    }

    public void validateInvariant() {
        if (data.length < KAPASITAS_MINIMAL) {
            throw new IllegalStateException("Kapasitas di bawah minimum");
        }
        if (size < 0 || size > data.length) {
            throw new IllegalStateException("size di luar rentang kapasitas");
        }
        for (int i = size; i < data.length; i++) {
            if (data[i] != null) {
                throw new IllegalStateException("Slot di atas puncak harus null (indeks " + i + ")");
            }
        }
    }

    private void resize(int kapasitasBaru) {
        Object[] baru = new Object[kapasitasBaru];
        for (int i = 0; i < size; i++) {
            baru[i] = data[i];
        }
        data = baru;
    }
}


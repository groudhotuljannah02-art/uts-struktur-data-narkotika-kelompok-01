package uts.ds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SinglyLinkedListTest {

    private SinglyLinkedList<Integer> isi(int... nilai) {
        SinglyLinkedList<Integer> l = new SinglyLinkedList<>();
        for (int n : nilai) {
            l.addLast(n);
        }
        l.validateInvariant();
        return l;
    }

    @Test
    void listBaruKosongDanValid() {
        SinglyLinkedList<Integer> l = new SinglyLinkedList<>();
        assertTrue(l.isEmpty());
        assertEquals(0, l.size());
        assertNull(l.peekFirst());
        assertNull(l.peekLast());
        l.validateInvariant();
    }

    @Test
    void addFirstDanAddLastMenjagaUrutanDanPointer() {
        SinglyLinkedList<Integer> l = new SinglyLinkedList<>();
        l.addFirst(2);
        l.addFirst(1);
        l.addLast(3);
        l.validateInvariant();
        assertEquals(3, l.size());
        assertEquals(1, l.get(0));
        assertEquals(2, l.get(1));
        assertEquals(3, l.get(2));
        assertEquals(1, l.peekFirst());
        assertEquals(3, l.peekLast());
    }

    @Test
    void insertDiAwalTengahAkhir() {
        SinglyLinkedList<Integer> l = isi(10, 30);
        l.insert(1, 20);
        l.insert(0, 5);
        l.insert(l.size(), 40);
        l.validateInvariant();
        assertEquals(5, l.size());
        assertEquals(5, l.get(0));
        assertEquals(20, l.get(2));
        assertEquals(40, l.get(4));
        assertEquals(40, l.peekLast());
    }

    @Test
    void insertPosisiTidakValidDitolakTanpaMerusakStruktur() {
        SinglyLinkedList<Integer> l = isi(1, 2);
        assertThrows(IndexOutOfBoundsException.class, () -> l.insert(-1, 9));
        assertThrows(IndexOutOfBoundsException.class, () -> l.insert(3, 9));
        assertEquals(2, l.size());
        l.validateInvariant();
    }

    @Test
    void removePertamaTengahTerakhir() {
        SinglyLinkedList<Integer> l = isi(1, 2, 3, 4, 5);
        assertEquals(1, l.remove(0));
        l.validateInvariant();
        assertEquals(3, l.remove(1));
        l.validateInvariant();
        assertEquals(5, l.remove(l.size() - 1));
        l.validateInvariant();
        assertEquals(2, l.size());
        assertEquals(2, l.peekFirst());
        assertEquals(4, l.peekLast());
    }

    @Test
    void regresiHapusTerakhirHarusMemperbaruiTail() {
        SinglyLinkedList<Integer> l = isi(1, 2, 3);
        l.remove(2);
        l.addLast(9);
        l.validateInvariant();
        assertEquals(3, l.size());
        assertEquals(9, l.get(2));
    }

    @Test
    void hapusSatuSatunyaDanListKembaliKosong() {
        SinglyLinkedList<Integer> l = isi(7);
        assertEquals(7, l.remove(0));
        assertTrue(l.isEmpty());
        assertNull(l.peekFirst());
        assertNull(l.peekLast());
        l.validateInvariant();
        l.addLast(8);
        l.validateInvariant();
    }

    @Test
    void getDanRemovePadaPosisiTidakValid() {
        SinglyLinkedList<Integer> l = isi(1, 2);
        assertThrows(IndexOutOfBoundsException.class, () -> l.get(2));
        assertThrows(IndexOutOfBoundsException.class, () -> l.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> l.remove(2));
        assertThrows(IndexOutOfBoundsException.class, () -> new SinglyLinkedList<Integer>().remove(0));
    }

    @Test
    void searchMelaporkanJumlahLangkah() {
        SinglyLinkedList<Integer> l = isi(10, 20, 30, 40);
        assertEquals(2, l.search(x -> x == 30));
        assertEquals(3, l.getLangkahTerakhir());
        assertEquals(-1, l.search(x -> x == 99));
        assertEquals(4, l.getLangkahTerakhir());
        assertEquals(3, l.indexOf(40));
        assertEquals(4, l.getLangkahTerakhir());
    }

    @Test
    void containsDanClear() {
        SinglyLinkedList<Integer> l = isi(1, 2, 3);
        assertTrue(l.contains(2));
        assertFalse(l.contains(5));
        l.clear();
        assertEquals(0, l.size());
        l.validateInvariant();
    }

    @Test
    void forEachMenelusuriSesuaiUrutan() {
        SinglyLinkedList<Integer> l = isi(1, 2, 3);
        StringBuilder sb = new StringBuilder();
        l.forEach(sb::append);
        assertEquals("123", sb.toString());
    }

    @Test
    void banyakOperasiAcakTetapValid() {
        SinglyLinkedList<Integer> l = new SinglyLinkedList<>();
        for (int i = 0; i < 200; i++) {
            if (i % 3 == 0) {
                l.addFirst(i);
            } else if (i % 3 == 1) {
                l.addLast(i);
            } else {
                l.insert(l.size() / 2, i);
            }
            if (i % 7 == 0 && l.size() > 2) {
                l.remove(l.size() / 2);
            }
            l.validateInvariant();
        }
    }
}


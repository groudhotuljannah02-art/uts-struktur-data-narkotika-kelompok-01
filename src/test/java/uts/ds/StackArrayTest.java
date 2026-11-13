package uts.ds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class StackArrayTest {

    @Test
    void pushPopLifo() {
        StackArray<String> s = new StackArray<>();
        s.push("a");
        s.push("b");
        s.push("c");
        s.validateInvariant();
        assertEquals("c", s.peek());
        assertEquals("c", s.pop());
        assertEquals("b", s.pop());
        assertEquals("a", s.pop());
        assertTrue(s.isEmpty());
        s.validateInvariant();
    }

    @Test
    void popDanPeekPadaStackKosongMelemparException() {
        StackArray<String> s = new StackArray<>();
        assertThrows(IllegalStateException.class, s::pop);
        assertThrows(IllegalStateException.class, s::peek);
    }

    @Test
    void kapasitasTumbuhLaluMenyusut() {
        StackArray<Integer> s = new StackArray<>();
        for (int i = 0; i < 100; i++) {
            s.push(i);
            s.validateInvariant();
        }
        assertEquals(100, s.size());
        assertTrue(s.kapasitas() >= 100);
        for (int i = 99; i >= 0; i--) {
            assertEquals(i, s.pop());
            s.validateInvariant();
        }
        assertEquals(StackArray.KAPASITAS_MINIMAL, s.kapasitas());
    }

    @Test
    void searchPreserveTidakMengubahUrutan() {
        StackArray<Integer> s = new StackArray<>();
        for (int i = 1; i <= 6; i++) {
            s.push(i);
        }
        assertEquals(2, s.searchPreserve(x -> x == 4)); // puncak=6, 5, 4
        assertEquals(3, s.getLangkahTerakhir());
        assertEquals(-1, s.searchPreserve(x -> x == 99));
        assertEquals(6, s.getLangkahTerakhir());
        assertEquals(6, s.size());
        for (int i = 0; i < 6; i++) {
            assertEquals(i + 1, s.get(i));
        }
        s.validateInvariant();
    }

    @Test
    void removeHanyaUntukElemenTeratas() {
        StackArray<Integer> s = new StackArray<>();
        s.add(1);
        s.add(2);
        assertThrows(IllegalArgumentException.class, () -> s.remove(0));
        assertEquals(2, s.remove(1));
        assertEquals(1, s.size());
    }

    @Test
    void containsGetDanClear() {
        StackArray<Integer> s = new StackArray<>();
        s.push(5);
        s.push(6);
        assertTrue(s.contains(5));
        assertFalse(s.contains(7));
        assertEquals(5, s.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> s.get(2));
        s.clear();
        assertTrue(s.isEmpty());
        s.validateInvariant();
    }
}

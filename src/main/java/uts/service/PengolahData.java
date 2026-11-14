package uts.service;

import uts.ds.SinglyLinkedList;
import uts.model.Putusan;
public final class PengolahData {

    private PengolahData() {
    }
    public static SinglyLinkedList<Putusan> filter(SinglyLinkedList<Putusan> sumber, Kriteria k) {
        SinglyLinkedList<Putusan> hasil = new SinglyLinkedList<>();
        sumber.forEach(p -> {
            if (k.cocok(p)) {
                hasil.addLast(p);
            }
        });
        return hasil;
    }

    private static final class Penghitung {
        final String jenis;
        int jumlah;

        Penghitung(String jenis) {
            this.jenis = jenis;
        }
    }

    public static Agregat agregasi(SinglyLinkedList<Putusan> sumber) {
        long[] acc = new long[3]; // 0 = jumlah, 1 = total vonis, 2 = total denda
        SinglyLinkedList<Penghitung> tmp = new SinglyLinkedList<>();
        sumber.forEach(p -> {
            acc[0]++;
            acc[1] += p.vonisBulan();
            acc[2] += p.dendaRupiah();
            String[] tokens = p.jenisTokens();
            if (tokens.length == 0) {
                tokens = new String[]{"(kosong)"};
            }
            for (String t : tokens) {
                int idx = tmp.search(c -> c.jenis.equalsIgnoreCase(t));
                if (idx < 0) {
                    Penghitung baru = new Penghitung(t);
                    baru.jumlah = 1;
                    tmp.addLast(baru);
                } else {
                    tmp.get(idx).jumlah++;
                }
            }
        });
        SinglyLinkedList<Agregat.JenisHitung> distribusi = new SinglyLinkedList<>();
        tmp.forEach(c -> distribusi.addLast(new Agregat.JenisHitung(c.jenis, c.jumlah)));
        double rata = acc[0] == 0 ? 0 : (double) acc[1] / acc[0];
        return new Agregat((int) acc[0], rata, acc[2], distribusi);
    }
    public static SinglyLinkedList<Putusan> urutkanVonis(SinglyLinkedList<Putusan> sumber, boolean naik) {
        SinglyLinkedList<Putusan> hasil = new SinglyLinkedList<>();
        sumber.forEach(p -> {
            int idx = hasil.search(q -> naik ? q.vonisBulan() > p.vonisBulan() : q.vonisBulan() < p.vonisBulan());
            if (idx < 0) {
                hasil.addLast(p);
            } else {
                hasil.insert(idx, p);
            }
        });
        return hasil;
    }

    public static String ringkasan(SinglyLinkedList<Putusan> data) {
        StringBuilder sb = new StringBuilder();
        int[] no = {0};
        data.forEach(p -> sb.append(++no[0]).append(". ").append(p.ringkas()).append('\n'));
        if (no[0] == 0) {
            sb.append("(kosong)\n");
        }
        return sb.toString();
    }
}
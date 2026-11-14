package uts.service;

import uts.model.Putusan;

public record Kriteria(
        String jenisNarkotika,
        Double beratMin,
        Double beratMax,
        String pasalTerbukti,
        Integer vonisMin,
        Integer vonisMax) {

    public Kriteria {
        if (beratMin != null && beratMax != null && beratMin > beratMax) {
            throw new IllegalArgumentException("beratMin tidak boleh lebih besar dari beratMax");
        }
        if (vonisMin != null && vonisMax != null && vonisMin > vonisMax) {
            throw new IllegalArgumentException("vonisMin tidak boleh lebih besar dari vonisMax");
        }
    }

    public static Kriteria kosong() {
        return new Kriteria(null, null, null, null, null, null);
    }

    public boolean cocok(Putusan p) {
        if (jenisNarkotika != null && !jenisNarkotika.isBlank()
                && !mengandung(p.jenisNarkotika(), jenisNarkotika)) {
            return false;
        }
        if (beratMin != null && p.beratBarangBuktiGram() < beratMin) {
            return false;
        }
        if (beratMax != null && p.beratBarangBuktiGram() > beratMax) {
            return false;
        }
        if (pasalTerbukti != null && !pasalTerbukti.isBlank()
                && !mengandung(p.pasalTerbukti(), pasalTerbukti)) {
            return false;
        }
        if (vonisMin != null && p.vonisBulan() < vonisMin) {
            return false;
        }
        return vonisMax == null || p.vonisBulan() <= vonisMax;
    }

    private static boolean mengandung(String sumber, String cari) {
        return sumber != null && sumber.toLowerCase().contains(cari.trim().toLowerCase());
    }
}


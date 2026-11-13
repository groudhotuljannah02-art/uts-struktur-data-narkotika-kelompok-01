package uts.model;

import java.time.LocalDate;

public record Putusan(
        String caseId,
        String nomorPerkara,
        String pengadilan,
        LocalDate tanggalPutusan,
        String terdakwaKode,
        String jenisNarkotika,
        double beratBarangBuktiGram,
        String pasalDakwaan,
        String pasalTerbukti,
        int tuntutanBulan,
        int vonisBulan,
        long dendaRupiah,
        String barangBukti,
        String keadaanMemberatkan,
        String keadaanMeringankan,
        String sourcePages) {

    public Putusan {
        if (kosong(caseId)) {
            throw new IllegalArgumentException("caseId wajib diisi");
        }
        if (kosong(nomorPerkara)) {
            throw new IllegalArgumentException("nomorPerkara wajib diisi");
        }
        if (Double.isNaN(beratBarangBuktiGram) || beratBarangBuktiGram < 0) {
            throw new IllegalArgumentException("beratBarangBuktiGram tidak boleh negatif");
        }
        if (tuntutanBulan < 0) {
            throw new IllegalArgumentException("tuntutanBulan tidak boleh negatif");
        }
        if (vonisBulan < 0) {
            throw new IllegalArgumentException("vonisBulan tidak boleh negatif");
        }
        if (dendaRupiah < 0) {
            throw new IllegalArgumentException("dendaRupiah tidak boleh negatif");
        }
    }

    public String[] jenisTokens() {
        if (kosong(jenisNarkotika)) {
            return new String[0];
        }
        String[] mentah = jenisNarkotika.split(";");
        int n = 0;
        for (String s : mentah) {
            if (!s.isBlank()) {
                n++;
            }
        }
        String[] hasil = new String[n];
        int i = 0;
        for (String s : mentah) {
            if (!s.isBlank()) {
                hasil[i++] = s.trim();
            }
        }
        return hasil;
    }

    public String ringkas() {
        return caseId + " | " + nomorPerkara + " | " + (pengadilan == null ? "-" : pengadilan)
                + " | " + (jenisNarkotika == null ? "-" : jenisNarkotika)
                + " | " + beratBarangBuktiGram + " g | vonis " + vonisBulan + " bln | denda Rp" + dendaRupiah;
    }

    private static boolean kosong(String s) {
        return s == null || s.isBlank();
    }
}

package uts.service;

import uts.ds.SinglyLinkedList;

public record Agregat(
        int jumlahPerkara,
        double rataRataVonisBulan,
        long totalDendaRupiah,
        SinglyLinkedList<JenisHitung> distribusiJenis) {

    public record JenisHitung(String jenis, int jumlah) {
    }

    public String ringkas() {
        StringBuilder sb = new StringBuilder();
        sb.append("Jumlah perkara   : ").append(jumlahPerkara).append('\n');
        sb.append(String.format("Rata-rata vonis  : %.2f bulan%n", rataRataVonisBulan));
        sb.append("Total denda      : Rp").append(totalDendaRupiah).append('\n');
        sb.append("Distribusi jenis :\n");
        distribusiJenis.forEach(j -> sb.append("  - ").append(j.jenis()).append(": ").append(j.jumlah()).append('\n'));
        return sb.toString();
    }
}

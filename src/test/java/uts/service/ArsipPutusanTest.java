package uts.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import uts.ds.SinglyLinkedList;
import uts.model.Putusan;

class ArsipPutusanTest {

    private static Putusan p(String id, String jenis, double berat, int vonis, long denda) {
        return new Putusan(id, "NO/" + id, "PN Uji", null, "T-" + id, jenis, berat,
                null, "Pasal 112", 0, vonis, denda, null, null, null, null);
    }

    private ArsipPutusan arsip50() {
        ArsipPutusan a = new ArsipPutusan();
        for (int i = 1; i <= 50; i++) {
            a.tambah(p("P" + i, i % 2 == 0 ? "Sabu" : "Ganja;Sabu", i, (i * 7) % 60, i * 1000L));
        }
        a.validateInvariant();
        return a;
    }

    @Test
    void muat50RecordSizeLimaPuluh() {
        assertEquals(50, arsip50().ukuran());
    }

    @Test
    void cariAdaTidakAdaDanBatasMelaporkanLangkah() {
        ArsipPutusan a = arsip50();
        assertEquals(1, a.cariCaseId("P1").langkah());
        ArsipPutusan.HasilCari batas = a.cariCaseId("P50");
        assertTrue(batas.ditemukan());
        assertEquals(49, batas.indeks());
        assertEquals(50, batas.langkah());
        ArsipPutusan.HasilCari tidak = a.cariNomorPerkara("TIDAK/ADA");
        assertFalse(tidak.ditemukan());
        assertEquals(50, tidak.langkah());
    }

    @Test
    void hapusPertamaTengahTerakhirLaluTambahKembali() {
        ArsipPutusan a = arsip50();
        Putusan pertama = a.hapus(0);
        Putusan tengah = a.hapus(a.ukuran() / 2);
        Putusan terakhir = a.hapus(a.ukuran() - 1);
        a.validateInvariant();
        assertEquals(47, a.ukuran());
        a.tambah(pertama);
        a.tambah(tengah);
        a.tambah(terakhir);
        a.validateInvariant();
        assertEquals(50, a.ukuran());
        assertEquals(terakhir, a.ambil(49));
        assertEquals(tengah, a.ambil(48));
        assertEquals(pertama, a.ambil(47));
    }

    @Test
    void recordTidakValidDitolakDanStrukturTetapKonsisten() {
        ArsipPutusan a = arsip50();
        assertThrows(IllegalArgumentException.class, () -> p("", "Sabu", 1, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> p("X", "Sabu", -1, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> a.tambah(p("P1", "Sabu", 1, 1, 1)));
        assertThrows(IllegalArgumentException.class, () -> a.tambah(null));
        assertEquals(50, a.ukuran());
        a.validateInvariant();
    }

    @Test
    void filterGabunganDanSumberTidakBerubah() {
        ArsipPutusan a = arsip50();
        Kriteria k = new Kriteria("Ganja", 10.0, 30.0, null, null, null);
        SinglyLinkedList<Putusan> hasil = PengolahData.filter(a.utama(), k);
        // Ganja hanya pada i ganjil; berat = i, rentang 10..30 => 11,13,...,29 = 10 record
        assertEquals(10, hasil.size());
        hasil.forEach(x -> assertTrue(x.jenisNarkotika().contains("Ganja")));
        assertEquals(50, a.ukuran());
        assertThrows(IllegalArgumentException.class, () -> new Kriteria(null, 5.0, 1.0, null, null, null));
    }

    @Test
    void agregasiMenghitungJumlahRataTotalDanDistribusi() {
        ArsipPutusan a = new ArsipPutusan();
        a.tambah(p("A", "Sabu", 1, 10, 100));
        a.tambah(p("B", "Sabu;Ganja", 1, 20, 200));
        a.tambah(p("C", null, 1, 30, 300));
        Agregat g = PengolahData.agregasi(a.utama());
        assertEquals(3, g.jumlahPerkara());
        assertEquals(20.0, g.rataRataVonisBulan(), 0.0001);
        assertEquals(600L, g.totalDendaRupiah());
        assertEquals(3, g.distribusiJenis().size()); // Sabu(2), Ganja(1), (kosong)(1)
        assertEquals(2, g.distribusiJenis().get(0).jumlah());
        assertEquals(0.0, PengolahData.agregasi(new SinglyLinkedList<>()).rataRataVonisBulan(), 0.0001);
    }

    @Test
    void urutkanVonisStabilDanTidakMerusakSumber() {
        ArsipPutusan a = arsip50();
        String awalSumber = a.utama().peekFirst().caseId();
        SinglyLinkedList<Putusan> naik = PengolahData.urutkanVonis(a.utama(), true);
        SinglyLinkedList<Putusan> turun = PengolahData.urutkanVonis(a.utama(), false);
        assertEquals(50, naik.size());
        for (int i = 1; i < naik.size(); i++) {
            assertTrue(naik.get(i - 1).vonisBulan() <= naik.get(i).vonisBulan());
            assertTrue(turun.get(i - 1).vonisBulan() >= turun.get(i).vonisBulan());
        }
        naik.validateInvariant();
        a.validateInvariant();
        assertEquals(awalSumber, a.utama().peekFirst().caseId());
    }

    @Test
    void auditMerekamOperasiDanPencarianStackTidakMengubahUrutan() {
        ArsipPutusan a = arsip50();
        a.hapus(0);
        a.cariCaseId("P10");
        int sebelum = a.jumlahAudit();
        String riwayatSebelum = a.riwayat();
        assertTrue(a.cariRiwayat("HAPUS") >= 0);
        assertEquals(sebelum, a.jumlahAudit());
        assertEquals(riwayatSebelum, a.riwayat());
        assertTrue(riwayatSebelum.split("\n")[0].contains("CARI_CASEID"));
    }

    @Test
    void imporCsvMenolakBarisTidakValidTanpaBerhenti(@TempDir Path dir) throws IOException {
        Path f = dir.resolve("uji.csv");
        String header = String.join(",", CsvPutusan.HEADER);
        String ok1 = "P1,N/1,PN A,2025-01-02,T1,Sabu,1.5,112,\"112, 114\",12,10,1000,pkt,,,\"1:2\"";
        String tglSalah = "P2,N/2,PN A,02-01-2025,T2,Sabu,1,,,0,0,0,,,,";
        String angkaSalah = "P3,N/3,PN A,2025-01-02,T3,Sabu,abc,,,0,0,0,,,,";
        String ganda = "P1,N/9,PN A,2025-01-02,T9,Sabu,1,,,0,0,0,,,,";
        String ok2 = "P4,N/4,,,T4,,,,,,,,,,,";
        Files.writeString(f, String.join("\n", header, ok1, tglSalah, angkaSalah, ganda, ok2) + "\n");
        ArsipPutusan a = new ArsipPutusan();
        CsvPutusan.HasilImpor h = CsvPutusan.impor(f, a);
        assertEquals(2, h.valid());
        assertEquals(3, h.ditolak());
        assertEquals(3, h.pesan().size());
        assertEquals(2, a.ukuran());
        assertEquals("112, 114", a.ambil(0).pasalTerbukti());
        assertEquals(0.0, a.ambil(1).beratBarangBuktiGram(), 0.0001);
        a.validateInvariant();
    }

    @Test
    void eksporLaluImporUlangMenghasilkanDataSama(@TempDir Path dir) throws IOException {
        ArsipPutusan a = new ArsipPutusan();
        a.tambah(new Putusan("P1", "N/1", "PN, A", java.time.LocalDate.of(2025, 5, 6), "T1", "Sabu",
                2.25, "112", "114 \"ayat\" 1", 24, 20, 800000000L, "1 paket", null, "sopan", "1:2"));
        Path f = dir.resolve("ekspor.csv");
        CsvPutusan.eksporCsv(f, a.utama());
        ArsipPutusan b = new ArsipPutusan();
        CsvPutusan.HasilImpor h = CsvPutusan.impor(f, b);
        assertEquals(1, h.valid());
        assertEquals(a.ambil(0), b.ambil(0));
    }
}

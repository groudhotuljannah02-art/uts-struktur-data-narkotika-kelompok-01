package uts.app;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import uts.ds.SinglyLinkedList;
import uts.model.Putusan;
import uts.service.ArsipPutusan;
import uts.service.CsvPutusan;
import uts.service.Kriteria;
import uts.service.PengolahData;

public final class DemoSkenario {

    private DemoSkenario() {
    }
    public static void jalankan(Path csv, PrintStream out) {
        if (!Files.exists(csv)) {
            out.println("File tidak ditemukan: " + csv);
            return;
        }
        ArsipPutusan arsip = new ArsipPutusan();
        judul(out, "1. Muat record dan buktikan size");
        try {
            CsvPutusan.HasilImpor h = CsvPutusan.impor(csv, arsip);
            out.println("valid=" + h.valid()
                    + " ditolak=" + h.ditolak()
                    + " size=" + arsip.ukuran()
                    + (arsip.ukuran() == 50
                    ? "  [OK size = 50]"
                    : "  [PERINGATAN: bukan 50]"));
            h.pesan().forEach(out::println);
        } catch (IOException e) {
            out.println("Gagal impor: " + e.getMessage());
            return;
        }
        if (arsip.ukuran() < 7) {
            out.println("Data terlalu sedikit untuk demo (minimal 7 record).");
            return;
        }

        arsip.validateInvariant();
        judul(out, "2. Cari: ada, tidak ada, posisi batas");
        Putusan awal = arsip.ambil(0);
        Putusan akhir = arsip.ambil(arsip.ukuran() - 1);
        laporCari(out, "ada (pertama)",
                arsip.cariNomorPerkara(awal.nomorPerkara()));
        laporCari(out, "tidak ada",
                arsip.cariNomorPerkara("TIDAK-ADA/0000"));
        laporCari(out, "batas (terakhir)",
                arsip.cariCaseId(akhir.caseId()));

        judul(out, "3. Hapus pertama, tengah, terakhir");
        statusPointer(out, "sebelum", arsip);
        Putusan hapusPertama = arsip.hapus(0);
        statusPointer(out, "setelah hapus pertama " + hapusPertama.caseId(), arsip);
        Putusan hapusTengah = arsip.hapus(arsip.ukuran() / 2);
        statusPointer(out, "setelah hapus tengah " + hapusTengah.caseId(), arsip);
        Putusan hapusTerakhir = arsip.hapus(arsip.ukuran() - 1);
        statusPointer(out, "setelah hapus terakhir " + hapusTerakhir.caseId(), arsip);
        arsip.validateInvariant();

        judul(out, "4. Tambah kembali tiga record");
        arsip.tambah(hapusPertama);
        arsip.tambah(hapusTengah);
        arsip.tambah(hapusTerakhir);
        arsip.validateInvariant();
        out.println("size=" + arsip.ukuran() + " invariant OK");
        out.println("Tiga elemen terakhir (urutan penambahan): "
                + arsip.ambil(arsip.ukuran() - 3).caseId() + ", "
                + arsip.ambil(arsip.ukuran() - 2).caseId() + ", "
                + arsip.ambil(arsip.ukuran() - 1).caseId());

        judul(out, "5. Filter gabungan (cocokkan dengan provenance manual)");
        String[] tokens = awal.jenisTokens();
        String jenis = tokens.length > 0 ? tokens[0] : null;
        Kriteria k = new Kriteria(
                jenis, null, null, null, awal.vonisBulan(), null
        );
        SinglyLinkedList<Putusan> hasil =
                PengolahData.filter(arsip.utama(), k);
        out.println("Kriteria: " + k);
        out.println("Hasil " + hasil.size() + " record; caseId:");
        hasil.forEach(p -> out.println(
                "  " + p.caseId()
                        + " (vonis " + p.vonisBulan()
                        + ", berat " + p.beratBarangBuktiGram() + ")"
        ));

        judul(out, "6. Pencarian pada Stack (audit) tidak mengubah urutan");
        String sebelum = arsip.riwayat();
        int posisi = arsip.cariRiwayat("HAPUS");
        String sesudah = arsip.riwayat();
        out.println("Posisi 'HAPUS' dari puncak = " + posisi
                + ", langkah = " + arsip.langkahCariRiwayat());
        out.println("Urutan stack identik sebelum/sesudah: "
                + sebelum.equals(sesudah));
        arsip.validateInvariant();

        judul(out, "7. Record tidak valid ditolak, struktur tetap konsisten");
        int ukuranSebelum = arsip.ukuran();
        try {
            arsip.tambah(new Putusan(
                    "", "X/1", null, null, null, null,
                    0, null, null, 0, 0, 0,
                    null, null, null, null
            ));
        } catch (IllegalArgumentException e) {
            out.println("Ditolak (caseId kosong): " + e.getMessage());
        }
        try {
            arsip.tambah(new Putusan(
                    awal.caseId(), "X/2", null, null, null, null,
                    0, null, null, 0, 0, 0,
                    null, null, null, null
            ));
        } catch (IllegalArgumentException e) {
            out.println("Ditolak (duplikat): " + e.getMessage());
        }
        try {
            arsip.tambah(new Putusan(
                    "BAD", "X/3", null, null, null, null,
                    -5, null, null, 0, 0, 0,
                    null, null, null, null
            ));
        } catch (IllegalArgumentException e) {
            out.println("Ditolak (berat negatif): " + e.getMessage());
        }
        arsip.validateInvariant();
        out.println("size sebelum=" + ukuranSebelum
                + ", sesudah=" + arsip.ukuran()
                + (ukuranSebelum == arsip.ukuran()
                ? "  [konsisten]"
                : "  [BERUBAH!]"));
    }

    private static void judul(PrintStream out, String teks) {
        out.println();
        out.println("=== " + teks + " ===");
    }
    private static void laporCari(
            PrintStream out,
            String label,
            ArsipPutusan.HasilCari h) {
        out.println(label + ": "
                + (h.ditemukan()
                ? "ketemu indeks " + h.indeks()
                : "tidak ditemukan")
                + ", langkah = " + h.langkah());
    }
    private static void statusPointer(
            PrintStream out,
            String label,
            ArsipPutusan arsip) {
        SinglyLinkedList<Putusan> u = arsip.utama();
        out.println(label + ": size=" + u.size()
                + ", head=" + u.peekFirst().caseId()
                + ", tail=" + u.peekLast().caseId());
    }
}




package uts.app;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import uts.ds.SinglyLinkedList;
import uts.model.Putusan;
import uts.service.Agregat;
import uts.service.ArsipPutusan;
import uts.service.CsvPutusan;
import uts.service.Kriteria;
import uts.service.PengolahData;

public class Main {

    private static final Scanner IN = new Scanner(System.in);
    private static final ArsipPutusan arsip = new ArsipPutusan();
    private static SinglyLinkedList<Putusan> hasilTerakhir = new SinglyLinkedList<>();
    private static Path csv = Path.of("data", "Data_Putusan_P01-P25_titikkoma.csv");

    public static void main(String[] args) {
        if (args.length > 0) {
            csv = Path.of(args[0]);
        }
        boolean jalan = true;
        while (jalan) {
            menu();
            String pilih = IN.nextLine().trim();
            try {
                switch (pilih) {
                    case "1" -> impor();
                    case "2" -> tampil();
                    case "3" -> tambah();
                    case "4" -> hapus();
                    case "5" -> cari();
                    case "6" -> filter();
                    case "7" -> agregasi();
                    case "8" -> urut();
                    case "9" -> System.out.print(arsip.riwayat());
                    case "10" -> ekspor();
                    case "11" -> DemoSkenario.jalankan(csv, System.out);
                    case "0" -> jalan = false;
                    default -> System.out.println("Pilihan tidak dikenal.");
                }
            } catch (IllegalArgumentException | IndexOutOfBoundsException | IOException e) {
                System.out.println("KESALAHAN: " + e.getMessage());
            }
        }
    }

    private static void menu() {
        System.out.println("""

                ===== ARSIP PUTUSAN NARKOTIKA =====
                1 Impor CSV      2 Tampil        3 Tambah       4 Hapus (posisi)
                5 Cari           6 Filter        7 Agregasi     8 Urut vonis
                9 Audit          10 Ekspor       11 Demo skenario   0 Keluar""");
        System.out.print("Pilih: ");
    }

    private static void impor() throws IOException {
        CsvPutusan.HasilImpor h = CsvPutusan.impor(csv, arsip);
        System.out.println("Valid: " + h.valid() + ", ditolak: " + h.ditolak() + ", size: " + arsip.ukuran());
        h.pesan().forEach(System.out::println);
        if (arsip.ukuran() != 50) {
            System.out.println("PERINGATAN: jumlah record = " + arsip.ukuran() + " (seharusnya 50)");
        }
    }

    private static void tampil() {
        hasilTerakhir = arsip.utama();
        System.out.print(PengolahData.ringkasan(arsip.utama()));
    }

    private static void tambah() {
        String id = tanya("caseId");
        String nomor = tanya("nomorPerkara");
        String pn = tanya("pengadilan");
        String jenis = tanya("jenisNarkotika (pisah ;)");
        double berat = angka(tanya("beratBarangBuktiGram (kosong=0)"));
        int vonis = (int) angka(tanya("vonisBulan (kosong=0)"));
        long denda = (long) angka(tanya("dendaRupiah (kosong=0)"));
        Putusan p = new Putusan(id, nomor, kosongNull(pn), null, null, kosongNull(jenis), berat,
                null, null, 0, vonis, denda, null, null, null, null);
        String posisi = tanya("posisi sisip (kosong = di akhir)");
        if (posisi.isBlank()) {
            arsip.tambah(p);
        } else {
            arsip.sisipkan(Integer.parseInt(posisi.trim()), p);
        }
        System.out.println("Ditambahkan. size = " + arsip.ukuran());
    }

    private static void hapus() {
        int pos = Integer.parseInt(tanya("posisi (0.." + (arsip.ukuran() - 1) + ")").trim());
        Putusan p = arsip.hapus(pos);
        System.out.println("Dihapus: " + p.ringkas() + " | size = " + arsip.ukuran());
    }

    private static void cari() {
        String kunci = tanya("nomorPerkara atau caseId");
        ArsipPutusan.HasilCari h = arsip.cariCaseId(kunci);
        if (!h.ditemukan()) {
            h = arsip.cariNomorPerkara(kunci);
        }
        if (h.ditemukan()) {
            System.out.println("Ketemu di indeks " + h.indeks() + " (" + h.langkah() + " langkah): " + h.putusan().ringkas());
        } else {
            System.out.println("Tidak ditemukan (" + h.langkah() + " langkah).");
        }
    }

    private static void filter() {
        String jenis = tanya("jenis narkotika (kosong=semua)");
        String bMin = tanya("berat min (gram, kosong=bebas)");
        String bMax = tanya("berat max (gram, kosong=bebas)");
        String pasal = tanya("pasal terbukti (kosong=semua)");
        String vMin = tanya("vonis min (bulan, kosong=bebas)");
        String vMax = tanya("vonis max (bulan, kosong=bebas)");
        Kriteria k = new Kriteria(kosongNull(jenis), desimalNull(bMin), desimalNull(bMax),
                kosongNull(pasal), bulatNull(vMin), bulatNull(vMax));
        hasilTerakhir = PengolahData.filter(arsip.utama(), k);
        arsip.catat("FILTER " + k + " -> " + hasilTerakhir.size() + " hasil");
        System.out.print(PengolahData.ringkasan(hasilTerakhir));
    }

    private static void agregasi() {
        Agregat a = PengolahData.agregasi(arsip.utama());
        System.out.print(a.ringkas());
        arsip.catat("AGREGASI");
    }

    private static void urut() {
        boolean naik = !tanya("urutan (n=naik, t=turun)").trim().equalsIgnoreCase("t");
        hasilTerakhir = PengolahData.urutkanVonis(arsip.utama(), naik);
        arsip.catat("URUT_VONIS " + (naik ? "naik" : "turun"));
        System.out.print(PengolahData.ringkasan(hasilTerakhir));
    }

    private static void ekspor() throws IOException {
        Path out = Path.of(tanya("nama file keluaran (.csv)").trim());
        CsvPutusan.eksporCsv(out, hasilTerakhir);
        arsip.catat("EKSPOR " + out + " (" + hasilTerakhir.size() + " baris)");
        System.out.println("Tersimpan: " + out.toAbsolutePath());
    }

    private static String tanya(String label) {
        System.out.print(label + ": ");
        return IN.nextLine();
    }

    private static String kosongNull(String s) {
        return s == null || s.isBlank() ? null : s.trim();
    }

    private static double angka(String s) {
        return s.isBlank() ? 0 : Double.parseDouble(s.trim());
    }

    private static Double desimalNull(String s) {
        return s.isBlank() ? null : Double.valueOf(s.trim());
    }

    private static Integer bulatNull(String s) {
        return s.isBlank() ? null : Integer.valueOf(s.trim());
    }
}

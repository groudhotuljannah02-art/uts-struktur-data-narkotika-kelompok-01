package uts.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import uts.ds.SinglyLinkedList;
import uts.model.Putusan;

public final class CsvPutusan {

    public static final String[] HEADER = {
            "caseId", "nomorPerkara", "pengadilan", "tanggalPutusan", "terdakwaKode", "jenisNarkotika",
            "beratBarangBuktiGram", "pasalDakwaan", "pasalTerbukti", "tuntutanBulan", "vonisBulan",
            "dendaRupiah", "barangBukti", "keadaanMemberatkan", "keadaanMeringankan", "sourcePages"
    };

    public record HasilImpor(int valid, int ditolak, SinglyLinkedList<String> pesan) {
    }

    private CsvPutusan() {
    }

    // ---------- impor ----------

    public static HasilImpor impor(Path file, ArsipPutusan arsip) throws IOException {
        SinglyLinkedList<String> pesan = new SinglyLinkedList<>();
        int valid = 0;
        int ditolak = 0;
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String header = br.readLine();
            if (header == null) {
                throw new IOException("File CSV kosong");
            }
            if (header.startsWith("\uFEFF")) {
                header = header.substring(1);
            }
            String[] kolom = parseBaris(header);
            int[] idx = petaKolom(kolom);
            String baris;
            int nomor = 1;
            while ((baris = br.readLine()) != null) {
                nomor++;
                if (baris.isBlank()) {
                    continue;
                }
                try {
                    String[] f = parseBaris(baris);
                    if (f.length != kolom.length) {
                        throw new IllegalArgumentException("jumlah kolom " + f.length + " != " + kolom.length);
                    }
                    arsip.tambah(bentuk(f, idx));
                    valid++;
                } catch (RuntimeException e) {
                    ditolak++;
                    pesan.addLast("Baris " + nomor + " ditolak: " + e.getMessage());
                }
            }
        }
        return new HasilImpor(valid, ditolak, pesan);
    }

    private static int[] petaKolom(String[] kolom) throws IOException {
        int[] idx = new int[HEADER.length];
        for (int i = 0; i < HEADER.length; i++) {
            idx[i] = -1;
            for (int j = 0; j < kolom.length; j++) {
                if (kolom[j].trim().equals(HEADER[i])) {
                    idx[i] = j;
                    break;
                }
            }
            if (idx[i] < 0) {
                throw new IOException("Kolom wajib tidak ada pada header: " + HEADER[i]);
            }
        }
        return idx;
    }

    private static Putusan bentuk(String[] f, int[] idx) {
        return new Putusan(
                teks(f, idx[0]), teks(f, idx[1]), teks(f, idx[2]),
                tanggal(f, idx[3]), teks(f, idx[4]), teks(f, idx[5]),
                desimal(f, idx[6], "beratBarangBuktiGram"), teks(f, idx[7]), teks(f, idx[8]),
                bulat(f, idx[9], "tuntutanBulan"), bulat(f, idx[10], "vonisBulan"),
                panjang(f, idx[11], "dendaRupiah"),
                teks(f, idx[12]), teks(f, idx[13]), teks(f, idx[14]), teks(f, idx[15]));
    }

    private static String teks(String[] f, int i) {
        String s = f[i].trim();
        return s.isEmpty() ? null : s;
    }

    private static LocalDate tanggal(String[] f, int i) {
        String s = f[i].trim();
        if (s.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(s);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("tanggalPutusan harus yyyy-MM-dd: '" + s + "'");
        }
    }

    private static double desimal(String[] f, int i, String nama) {
        String s = f[i].trim();
        if (s.isEmpty()) {
            return 0;
        }
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nama + " bukan angka: '" + s + "'");
        }
    }

    private static int bulat(String[] f, int i, String nama) {
        String s = f[i].trim();
        if (s.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nama + " bukan bilangan bulat: '" + s + "'");
        }
    }

    private static long panjang(String[] f, int i, String nama) {
        String s = f[i].trim();
        if (s.isEmpty()) {
            return 0;
        }
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nama + " bukan bilangan bulat: '" + s + "'");
        }
    }

    /** Parser satu baris CSV dengan dukungan tanda kutip dan "" sebagai kutip ter-escape. */
    static String[] parseBaris(String baris) {
        int jumlah = 1;
        boolean kutip = false;
        for (int i = 0; i < baris.length(); i++) {
            char c = baris.charAt(i);
            if (c == '"') {
                kutip = !kutip;
            } else if (c == ',' && !kutip) {
                jumlah++;
            }
        }
        String[] hasil = new String[jumlah];
        StringBuilder sb = new StringBuilder();
        int k = 0;
        kutip = false;
        for (int i = 0; i < baris.length(); i++) {
            char c = baris.charAt(i);
            if (kutip) {
                if (c == '"') {
                    if (i + 1 < baris.length() && baris.charAt(i + 1) == '"') {
                        sb.append('"');
                        i++;
                    } else {
                        kutip = false;
                    }
                } else {
                    sb.append(c);
                }
            } else if (c == '"') {
                kutip = true;
            } else if (c == ',') {
                hasil[k++] = sb.toString();
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        hasil[k] = sb.toString();
        return hasil;
    }

    // ---------- ekspor ----------

    public static void eksporCsv(Path file, SinglyLinkedList<Putusan> data) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            w.write(String.join(",", HEADER));
            w.newLine();
            IOException[] gagal = new IOException[1];
            data.forEach(p -> {
                if (gagal[0] != null) {
                    return;
                }
                try {
                    w.write(baris(p));
                    w.newLine();
                } catch (IOException e) {
                    gagal[0] = e;
                }
            });
            if (gagal[0] != null) {
                throw gagal[0];
            }
        }
    }

    public static void eksporTeks(Path file, String isi) throws IOException {
        Files.writeString(file, isi, StandardCharsets.UTF_8);
    }

    private static String baris(Putusan p) {
        return String.join(",",
                esc(p.caseId()), esc(p.nomorPerkara()), esc(p.pengadilan()),
                p.tanggalPutusan() == null ? "" : p.tanggalPutusan().toString(),
                esc(p.terdakwaKode()), esc(p.jenisNarkotika()),
                Double.toString(p.beratBarangBuktiGram()), esc(p.pasalDakwaan()), esc(p.pasalTerbukti()),
                Integer.toString(p.tuntutanBulan()), Integer.toString(p.vonisBulan()),
                Long.toString(p.dendaRupiah()),
                esc(p.barangBukti()), esc(p.keadaanMemberatkan()), esc(p.keadaanMeringankan()),
                esc(p.sourcePages()));
    }

    private static String esc(String s) {
        if (s == null) {
            return "";
        }
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }
}

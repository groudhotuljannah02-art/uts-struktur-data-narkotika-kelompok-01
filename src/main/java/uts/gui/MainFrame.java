package uts.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;
import java.nio.file.Path;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import uts.ds.SinglyLinkedList;
import uts.model.Putusan;
import uts.service.ArsipPutusan;
import uts.service.CsvPutusan;
import uts.service.Kriteria;
import uts.service.PengolahData;

public class MainFrame extends JFrame {

    private final ArsipPutusan arsip = new ArsipPutusan();
    private SinglyLinkedList<Putusan> hasilTerakhir = new SinglyLinkedList<>();

    private final JTextField fileField = new JTextField("data/putusan-50.csv", 22);
    private final JTextField cariField = new JTextField(12);
    private final JTextField jenisField = new JTextField(8);
    private final JTextField vonisMinField = new JTextField(4);
    private final JTextField vonisMaxField = new JTextField(4);
    private final JTextField posisiField = new JTextField(4);
    private final JTextArea output = new JTextArea();

    public MainFrame() {
        super("Arsip Putusan Narkotika - Singly Linked List");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(980, 620);
        setLocationRelativeTo(null);
        output.setEditable(false);
        output.setFont(new java.awt.Font(java.awt.Font.MONOSPACED, java.awt.Font.PLAIN, 12));

        JPanel atas = new JPanel(new java.awt.GridLayout(3, 1));
        atas.add(baris(new JLabel("CSV:"), fileField, tombol("Impor", this::impor),
                tombol("Tampil", this::tampil), tombol("Agregasi", this::agregasi)));
        atas.add(baris(new JLabel("Cari (caseId / nomor):"), cariField, tombol("Cari", this::cari),
                new JLabel("Hapus posisi:"), posisiField, tombol("Hapus", this::hapus)));
        atas.add(baris(new JLabel("Filter jenis:"), jenisField, new JLabel("vonis min:"), vonisMinField,
                new JLabel("max:"), vonisMaxField, tombol("Filter", this::filter),
                tombol("Urut vonis", this::urut), tombol("Audit", this::audit), tombol("Ekspor", this::ekspor)));

        add(atas, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);
    }

    private JPanel baris(java.awt.Component... komponen) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (java.awt.Component c : komponen) {
            p.add(c);
        }
        return p;
    }

    private JButton tombol(String teks, Aksi aksi) {
        JButton b = new JButton(teks);
        b.addActionListener(e -> {
            try {
                aksi.jalan();
            } catch (IllegalArgumentException | IndexOutOfBoundsException | IOException ex) {
                output.setText("KESALAHAN: " + ex.getMessage());
            }
        });
        return b;
    }

    @FunctionalInterface
    private interface Aksi {
        void jalan() throws IOException;
    }

    private void impor() throws IOException {
        CsvPutusan.HasilImpor h = CsvPutusan.impor(Path.of(fileField.getText().trim()), arsip);
        StringBuilder sb = new StringBuilder("Valid: " + h.valid() + ", ditolak: " + h.ditolak()
                + ", size: " + arsip.ukuran() + "\n");
        h.pesan().forEach(m -> sb.append(m).append('\n'));
        output.setText(sb.toString());
    }

    private void tampil() {
        hasilTerakhir = arsip.utama();
        output.setText(PengolahData.ringkasan(arsip.utama()));
    }

    private void cari() {
        String kunci = cariField.getText().trim();
        ArsipPutusan.HasilCari h = arsip.cariCaseId(kunci);
        if (!h.ditemukan()) {
            h = arsip.cariNomorPerkara(kunci);
        }
        output.setText(h.ditemukan()
                ? "Ketemu indeks " + h.indeks() + " dalam " + h.langkah() + " langkah:\n" + h.putusan().ringkas()
                : "Tidak ditemukan setelah " + h.langkah() + " langkah.");
    }

    private void hapus() {
        Putusan p = arsip.hapus(Integer.parseInt(posisiField.getText().trim()));
        output.setText("Dihapus: " + p.ringkas() + "\nsize = " + arsip.ukuran());
    }

    private void filter() {
        Kriteria k = new Kriteria(kosongNull(jenisField.getText()), null, null, null,
                bulatNull(vonisMinField.getText()), bulatNull(vonisMaxField.getText()));
        hasilTerakhir = PengolahData.filter(arsip.utama(), k);
        arsip.catat("FILTER " + k + " -> " + hasilTerakhir.size() + " hasil");
        output.setText(PengolahData.ringkasan(hasilTerakhir));
    }

    private void urut() {
        hasilTerakhir = PengolahData.urutkanVonis(arsip.utama(), true);
        arsip.catat("URUT_VONIS naik");
        output.setText(PengolahData.ringkasan(hasilTerakhir));
    }

    private void agregasi() {
        output.setText(PengolahData.agregasi(arsip.utama()).ringkas());
        arsip.catat("AGREGASI");
    }

    private void audit() {
        output.setText(arsip.riwayat());
    }

    private void ekspor() throws IOException {
        Path out = Path.of("hasil-ekspor.csv");
        CsvPutusan.eksporCsv(out, hasilTerakhir);
        arsip.catat("EKSPOR " + out + " (" + hasilTerakhir.size() + " baris)");
        output.setText("Tersimpan: " + out.toAbsolutePath());
    }

    private static String kosongNull(String s) {
        return s == null || s.isBlank() ? null : s.trim();
    }

    private static Integer bulatNull(String s) {
        return s == null || s.isBlank() ? null : Integer.valueOf(s.trim());
    }
}

package uts.service;
import java.time.LocalDateTime;
import uts.ds.SinglyLinkedList;
import uts.ds.StackArray;
import uts.model.Putusan;
public class ArsipPutusan {

    public record HasilCari(int indeks, Putusan putusan, long langkah) {
        public boolean ditemukan() {
            return indeks >= 0;
        }
    }

    private final SinglyLinkedList<Putusan> utama = new SinglyLinkedList<>();
    private final StackArray<String> audit = new StackArray<>();

    public void tambah(Putusan p) {
        cekBaru(p);
        utama.addLast(p);
        catat("TAMBAH_AKHIR " + p.caseId());
    }

    public void tambahDiAwal(Putusan p) {
        cekBaru(p);
        utama.addFirst(p);
        catat("TAMBAH_AWAL " + p.caseId());
    }

    public void sisipkan(int posisi, Putusan p) {
        cekBaru(p);
        utama.insert(posisi, p);
        catat("SISIP posisi=" + posisi + " " + p.caseId());
    }

    public Putusan hapus(int posisi) {
        Putusan p = utama.remove(posisi);
        catat("HAPUS posisi=" + posisi + " " + p.caseId());
        return p;
    }

    public HasilCari cariCaseId(String caseId) {
        int idx = utama.search(p -> p.caseId().equalsIgnoreCase(caseId));
        return hasilCari("CARI_CASEID " + caseId, idx);
    }

    public HasilCari cariNomorPerkara(String nomor) {
        int idx = utama.search(p -> p.nomorPerkara().equalsIgnoreCase(nomor));
        return hasilCari("CARI_NOMOR " + nomor, idx);
    }

    private HasilCari hasilCari(String aksi, int idx) {
        long langkah = utama.getLangkahTerakhir();
        catat(aksi + " -> " + (idx >= 0 ? "ketemu@" + idx : "tidak ada") + " langkah=" + langkah);
        return new HasilCari(idx, idx >= 0 ? utama.get(idx) : null, langkah);
    }

    public SinglyLinkedList<Putusan> utama() {
        return utama;
    }
    public Putusan ambil(int posisi) {
        return utama.get(posisi);
    }
    public int ukuran() {
        return utama.size();
    }

    public void catat(String aksi) {
        audit.push(LocalDateTime.now() + " | " + aksi);
    }
    public String riwayat() {
        StringBuilder sb = new StringBuilder();
        for (int i = audit.size() - 1; i >= 0; i--) {
            sb.append(audit.size() - i).append(". ").append(audit.get(i)).append('\n');
        }
        return sb.toString();
    }

    public int jumlahAudit() {
        return audit.size();
    }
    public int cariRiwayat(String kata) {
        return audit.searchPreserve(s -> s.contains(kata));
    }
    public long langkahCariRiwayat() {
        return audit.getLangkahTerakhir();
    }
    public void validateInvariant() {
        utama.validateInvariant();
        audit.validateInvariant();
    }

    private void cekBaru(Putusan p) {
        if (p == null) {
            catat("GAGAL_TAMBAH: record null");
            throw new IllegalArgumentException("Record null ditolak");
        }
        if (utama.search(x -> x.caseId().equalsIgnoreCase(p.caseId())) >= 0) {
            catat("GAGAL_TAMBAH: caseId ganda " + p.caseId());
            throw new IllegalArgumentException("caseId sudah ada: " + p.caseId());
        }
    }
}

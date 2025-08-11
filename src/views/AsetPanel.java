package views;

import config.Database;
import controllers.AsetController;
import controllers.JenisAsetController;
import controllers.KondisiController;
import models.Aset;
import models.JenisAset;
import models.Kondisi;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import java.sql.Connection;

public class AsetPanel extends JPanel {

    private AsetController asetController;
    private JenisAsetController jenisAsetController;
    private KondisiController kondisiController;
    private JTable table;
    private DefaultTableModel tableModel;

    public AsetPanel() {
        asetController = new AsetController();
        jenisAsetController = new JenisAsetController();
        kondisiController = new KondisiController();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Title
        JLabel lblTitle = new JLabel("Master Data Aset", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitle, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
                new Object[]{
                    "Kode Aset", "Nama Aset", "Jenis", "Kondisi", "Lokasi",
                    "Tahun", "Nilai", "Status", "Update Terakhir"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 5) {
                    return Integer.class;
                }
                if (columnIndex == 6) {
                    return Double.class;
                }
                return String.class;
            }
        };

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnAdd = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Hapus");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnCetak = new JButton("Cetak");
        JButton btnNilai = new JButton("Nilai Kelayakan");

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnNilai);
        buttonPanel.add(btnCetak);

        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddEditDialog(null);
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String kode = (String) tableModel.getValueAt(selectedRow, 0);
                    Aset aset = asetController.getByKode(kode);
                    showAddEditDialog(aset);
                } else {
                    JOptionPane.showMessageDialog(
                            AsetPanel.this,
                            "Pilih data yang akan diedit",
                            "Peringatan",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String kode = (String) tableModel.getValueAt(selectedRow, 0);
                    int confirm = JOptionPane.showConfirmDialog(
                            AsetPanel.this,
                            "Yakin ingin menghapus data ini?",
                            "Konfirmasi",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        if (asetController.delete(kode)) {
                            JOptionPane.showMessageDialog(
                                    AsetPanel.this,
                                    "Data berhasil dihapus",
                                    "Sukses",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                            loadData();
                        } else {
                            JOptionPane.showMessageDialog(
                                    AsetPanel.this,
                                    "Gagal menghapus data",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            AsetPanel.this,
                            "Pilih data yang akan dihapus",
                            "Peringatan",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });

        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });

        btnCetak.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printReport();
            }

        });

        btnNilai.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String kode = (String) tableModel.getValueAt(selectedRow, 0);
                    Aset aset = asetController.getByKode(kode);
                    if (aset != null && aset.getKodeKondisi() != null) {
                        nilaiKelayakan(aset);
                    } else {
                        JOptionPane.showMessageDialog(
                                AsetPanel.this,
                                "Aset harus memiliki kondisi terlebih dahulu",
                                "Peringatan",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            AsetPanel.this,
                            "Pilih aset yang akan dinilai",
                            "Peringatan",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });
    }

    private void printReport() {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            // Path file jrxml dan jasper
            String jrxmlPath = "./src/reports/reportaset.jrxml";
            String jasperPath = "./src/reports/reportaset.jasper";

            // Compile jrxml ke jasper (auto jika ada perubahan)
            JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);

            // 2. Prepare parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("LOGO_DIR", "src/image/logo_bmn.png"); // path ke logo

            // 4. Fill report
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperPath,
                    parameters,
                    conn
            );

            // 5. Preview report
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error mencetak laporan: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Aset> asetList = asetController.getAllWithDetails();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        for (Aset aset : asetList) {
            String updatedAt = aset.getUpdatedAt() != null
                    ? sdf.format(aset.getUpdatedAt()) : "-";

            tableModel.addRow(new Object[]{
                aset.getKodeAset(),
                aset.getNamaAset(),
                aset.getNamaJenis(),
                aset.getNamaKondisi(),
                aset.getLokasi(),
                aset.getTahunPerolehan(),
                aset.getNilaiPerolehan(),
                aset.getStatusKelayakan(),
                updatedAt
            });
        }
    }

    private void showAddEditDialog(Aset aset) {
        JDialog dialog = new JDialog();
        dialog.setTitle(aset == null ? "Tambah Data Aset" : "Edit Data Aset");
        dialog.setModal(true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Kode Aset
        JLabel lblKode = new JLabel("Kode Aset:");
        JTextField txtKode = new JTextField();

        // Nama Aset
        JLabel lblNama = new JLabel("Nama Aset:");
        JTextField txtNama = new JTextField();

        // Jenis Aset
        JLabel lblJenis = new JLabel("Jenis Aset:");
        JComboBox<String> cbJenis = new JComboBox<>();
        List<JenisAset> jenisList = jenisAsetController.getAll();
        for (JenisAset jenis : jenisList) {
            cbJenis.addItem(jenis.getKodeJenis() + " - " + jenis.getNamaJenis());
        }

        // Kondisi
        JLabel lblKondisi = new JLabel("Kondisi:");
        JComboBox<String> cbKondisi = new JComboBox<>();
        List<Kondisi> kondisiList = kondisiController.getAll();
        cbKondisi.addItem("- Pilih Kondisi -");
        for (Kondisi kondisi : kondisiList) {
            cbKondisi.addItem(kondisi.getKodeKondisi() + " - " + kondisi.getNamaKondisi());
        }

        // Lokasi
        JLabel lblLokasi = new JLabel("Lokasi:");
        JTextField txtLokasi = new JTextField();

        // Tahun Perolehan
        JLabel lblTahun = new JLabel("Tahun Perolehan:");
        JSpinner spTahun = new JSpinner(
                new SpinnerNumberModel(2023, 1900, 2100, 1)
        );

        // Nilai Perolehan
        JLabel lblNilai = new JLabel("Nilai Perolehan:");
        JTextField txtNilai = new JTextField();

        if (aset != null) {
            txtKode.setText(aset.getKodeAset());
            txtNama.setText(aset.getNamaAset());

            // Set jenis aset
            for (int i = 0; i < cbJenis.getItemCount(); i++) {
                if (cbJenis.getItemAt(i).startsWith(aset.getKodeJenis())) {
                    cbJenis.setSelectedIndex(i);
                    break;
                }
            }

            // Set kondisi
            if (aset.getKodeKondisi() != null) {
                for (int i = 1; i < cbKondisi.getItemCount(); i++) {
                    if (cbKondisi.getItemAt(i).startsWith(aset.getKodeKondisi())) {
                        cbKondisi.setSelectedIndex(i);
                        break;
                    }
                }
            }

            txtLokasi.setText(aset.getLokasi());
            spTahun.setValue(aset.getTahunPerolehan());
            txtNilai.setText(String.valueOf(aset.getNilaiPerolehan()));

            txtKode.setEditable(false);
        }

        panel.add(lblKode);
        panel.add(txtKode);
        panel.add(lblNama);
        panel.add(txtNama);
        panel.add(lblJenis);
        panel.add(cbJenis);
        panel.add(lblKondisi);
        panel.add(cbKondisi);
        panel.add(lblLokasi);
        panel.add(txtLokasi);
        panel.add(lblTahun);
        panel.add(spTahun);
        panel.add(lblNilai);
        panel.add(txtNilai);

        JButton btnSave = new JButton("Simpan");
        JButton btnCancel = new JButton("Batal");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kode = txtKode.getText().trim();
                String nama = txtNama.getText().trim();
                String jenis = cbJenis.getSelectedItem() != null
                        ? cbJenis.getSelectedItem().toString().split(" - ")[0] : "";
                String kondisi = cbKondisi.getSelectedIndex() > 0
                        ? cbKondisi.getSelectedItem().toString().split(" - ")[0] : null;
                String lokasi = txtLokasi.getText().trim();
                int tahun = (int) spTahun.getValue();
                double nilai = 0;

                try {
                    nilai = Double.parseDouble(txtNilai.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            dialog,
                            "Nilai perolehan harus angka",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                if (kode.isEmpty() || nama.isEmpty() || jenis.isEmpty() || lokasi.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            dialog,
                            "Semua field kecuali kondisi harus diisi",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                Aset newAset = new Aset();
                newAset.setKodeAset(kode);
                newAset.setNamaAset(nama);
                newAset.setKodeJenis(jenis);
                newAset.setKodeKondisi(kondisi);
                newAset.setLokasi(lokasi);
                newAset.setTahunPerolehan(tahun);
                newAset.setNilaiPerolehan(nilai);

                if (aset == null) {
                    // Add new
                    if (asetController.create(newAset)) {
                        JOptionPane.showMessageDialog(
                                dialog,
                                "Data berhasil ditambahkan",
                                "Sukses",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        loadData();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(
                                dialog,
                                "Gagal menambahkan data",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } else {
                    // Update
                    if (asetController.update(newAset)) {
                        JOptionPane.showMessageDialog(
                                dialog,
                                "Data berhasil diperbarui",
                                "Sukses",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        loadData();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(
                                dialog,
                                "Gagal memperbarui data",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }

    private void nilaiKelayakan(Aset aset) {
        // Implementasi forward chaining untuk penilaian kelayakan
        String hasil = asetController.nilaiKelayakan(aset);

        if (hasil != null) {
            // Update status di database
            asetController.updateStatusKelayakan(aset.getKodeAset(), hasil);

            // Tambahkan ke riwayat penilaian
            asetController.tambahPenilaian(aset.getIdAset(), hasil, "Penilaian otomatis");

            JOptionPane.showMessageDialog(
                    this,
                    "Hasil penilaian: " + hasil,
                    "Hasil Penilaian",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadData();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Tidak dapat menentukan kelayakan",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

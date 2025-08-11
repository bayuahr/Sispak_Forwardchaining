package views;

import config.Database;
import controllers.AturanController;
import controllers.JenisAsetController;
import models.Aturan;
import models.JenisAset;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class AturanPanel extends JPanel {

    private AturanController controller;
    private JenisAsetController jenisAsetController;
    private JTable table;
    private DefaultTableModel tableModel;

    public AturanPanel() {
        controller = new AturanController();
        jenisAsetController = new JenisAsetController();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Title
        JLabel lblTitle = new JLabel("Master Aturan Penilaian", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitle, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
                new Object[]{
                    "Kode Aturan", "Kondisi", "Jenis Aset", "Kesimpulan",
                    "Prioritas", "Aktif"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) {
                    return Integer.class;
                }
                if (columnIndex == 5) {
                    return Boolean.class;
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
        JButton btnAktifkan = new JButton("Aktifkan/Nonaktifkan");
        JButton btnCetak = new JButton("Cetak");

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnAktifkan);
        buttonPanel.add(btnCetak);
        add(buttonPanel, BorderLayout.SOUTH);
        btnCetak.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printReport();
            }

        });
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
                    Aturan aturan = controller.getByKode(kode);
                    showAddEditDialog(aturan);
                } else {
                    JOptionPane.showMessageDialog(
                            AturanPanel.this,
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
                            AturanPanel.this,
                            "Yakin ingin menghapus data ini?",
                            "Konfirmasi",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        if (controller.delete(kode)) {
                            JOptionPane.showMessageDialog(
                                    AturanPanel.this,
                                    "Data berhasil dihapus",
                                    "Sukses",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                            loadData();
                        } else {
                            JOptionPane.showMessageDialog(
                                    AturanPanel.this,
                                    "Gagal menghapus data",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            AturanPanel.this,
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

        btnAktifkan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String kode = (String) tableModel.getValueAt(selectedRow, 0);
                    boolean currentStatus = (boolean) tableModel.getValueAt(selectedRow, 5);

                    if (controller.toggleAktif(kode, !currentStatus)) {
                        JOptionPane.showMessageDialog(
                                AturanPanel.this,
                                "Status aturan berhasil diubah",
                                "Sukses",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        loadData();
                    } else {
                        JOptionPane.showMessageDialog(
                                AturanPanel.this,
                                "Gagal mengubah status aturan",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            AturanPanel.this,
                            "Pilih aturan yang akan diaktifkan/nonaktifkan",
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
            String jrxmlPath = "./src/reports/reportaturan.jrxml";
            String jasperPath = "./src/reports/reportaturan.jasper";

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
        List<Aturan> aturanList = controller.getAllWithDetails();

        for (Aturan aturan : aturanList) {
            tableModel.addRow(new Object[]{
                aturan.getKodeAturan(),
                aturan.getKondisi(),
                aturan.getNamaJenis() != null ? aturan.getNamaJenis() : "Semua Jenis",
                aturan.getKesimpulan(),
                aturan.getPrioritas(),
                aturan.isAktif()
            });
        }
    }

    private void showAddEditDialog(Aturan aturan) {
        JDialog dialog = new JDialog();
        dialog.setTitle(aturan == null ? "Tambah Aturan" : "Edit Aturan");
        dialog.setModal(true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Kode Aturan
        JLabel lblKode = new JLabel("Kode Aturan:");
        JTextField txtKode = new JTextField();

        // Kondisi
        JLabel lblKondisi = new JLabel("Kondisi (pisah koma untuk OR):");
        JTextField txtKondisi = new JTextField();

        // Jenis Aset
        JLabel lblJenis = new JLabel("Jenis Aset (kosongkan untuk semua):");
        JComboBox<String> cbJenis = new JComboBox<>();
        cbJenis.addItem("- Semua Jenis -");
        List<JenisAset> jenisList = jenisAsetController.getAll();
        for (JenisAset jenis : jenisList) {
            cbJenis.addItem(jenis.getKodeJenis() + " - " + jenis.getNamaJenis());
        }

        // Kesimpulan
        JLabel lblKesimpulan = new JLabel("Kesimpulan:");
        JTextField txtKesimpulan = new JTextField();

        // Prioritas
        JLabel lblPrioritas = new JLabel("Prioritas:");
        JSpinner spPrioritas = new JSpinner(
                new SpinnerNumberModel(10, 1, 100, 1)
        );

        // Aktif
        JLabel lblAktif = new JLabel("Aktif:");
        JCheckBox chkAktif = new JCheckBox();

        if (aturan != null) {
            txtKode.setText(aturan.getKodeAturan());
            txtKondisi.setText(aturan.getKondisi());

            if (aturan.getKodeJenis() != null) {
                for (int i = 1; i < cbJenis.getItemCount(); i++) {
                    if (cbJenis.getItemAt(i).startsWith(aturan.getKodeJenis())) {
                        cbJenis.setSelectedIndex(i);
                        break;
                    }
                }
            }

            txtKesimpulan.setText(aturan.getKesimpulan());
            spPrioritas.setValue(aturan.getPrioritas());
            chkAktif.setSelected(aturan.isAktif());

            txtKode.setEditable(false);
        }

        panel.add(lblKode);
        panel.add(txtKode);
        panel.add(lblKondisi);
        panel.add(txtKondisi);
        panel.add(lblJenis);
        panel.add(cbJenis);
        panel.add(lblKesimpulan);
        panel.add(txtKesimpulan);
        panel.add(lblPrioritas);
        panel.add(spPrioritas);
        panel.add(lblAktif);
        panel.add(chkAktif);

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
                String kondisi = txtKondisi.getText().trim();
                String jenis = cbJenis.getSelectedIndex() > 0
                        ? cbJenis.getSelectedItem().toString().split(" - ")[0] : null;
                String kesimpulan = txtKesimpulan.getText().trim();
                int prioritas = (int) spPrioritas.getValue();
                boolean aktif = chkAktif.isSelected();

                if (kode.isEmpty() || kondisi.isEmpty() || kesimpulan.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            dialog,
                            "Kode, kondisi, dan kesimpulan harus diisi",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                Aturan newAturan = new Aturan();
                newAturan.setKodeAturan(kode);
                newAturan.setKondisi(kondisi);
                newAturan.setKodeJenis(jenis);
                newAturan.setKesimpulan(kesimpulan);
                newAturan.setPrioritas(prioritas);
                newAturan.setAktif(aktif);

                if (aturan == null) {
                    // Add new
                    if (controller.create(newAturan)) {
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
                    if (controller.update(newAturan)) {
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
}

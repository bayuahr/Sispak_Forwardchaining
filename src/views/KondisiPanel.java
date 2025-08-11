package views;

import config.Database;
import controllers.KondisiController;
import models.Kondisi;
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

public class KondisiPanel extends JPanel {

    private KondisiController controller;
    private JTable table;
    private DefaultTableModel tableModel;

    public KondisiPanel() {
        controller = new KondisiController();
        initComponents();
        loadData();
    }

    private void printReport() {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            // Path file jrxml dan jasper
            String jrxmlPath = "./src/reports/reportkondisi.jrxml";
            String jasperPath = "./src/reports/reportkondisi.jasper";

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

    private void initComponents() {
        setLayout(new BorderLayout());

        // Title
        JLabel lblTitle = new JLabel("Master Kondisi Aset", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitle, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
                new Object[]{"Kode Kondisi", "Nama Kondisi"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
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

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
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
                    Kondisi kondisi = controller.getByKode(kode);
                    showAddEditDialog(kondisi);
                } else {
                    JOptionPane.showMessageDialog(
                            KondisiPanel.this,
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
                            KondisiPanel.this,
                            "Yakin ingin menghapus data ini?",
                            "Konfirmasi",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        if (controller.delete(kode)) {
                            JOptionPane.showMessageDialog(
                                    KondisiPanel.this,
                                    "Data berhasil dihapus",
                                    "Sukses",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                            loadData();
                        } else {
                            JOptionPane.showMessageDialog(
                                    KondisiPanel.this,
                                    "Gagal menghapus data",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            KondisiPanel.this,
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
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Kondisi> kondisiList = controller.getAll();

        for (Kondisi kondisi : kondisiList) {
            tableModel.addRow(new Object[]{
                kondisi.getKodeKondisi(),
                kondisi.getNamaKondisi()
            });
        }
    }

    private void showAddEditDialog(Kondisi kondisi) {
        JDialog dialog = new JDialog();
        dialog.setTitle(kondisi == null ? "Tambah Kondisi Aset" : "Edit Kondisi Aset");
        dialog.setModal(true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblKode = new JLabel("Kode Kondisi:");
        JTextField txtKode = new JTextField();
        JLabel lblNama = new JLabel("Nama Kondisi:");
        JTextField txtNama = new JTextField();

        if (kondisi != null) {
            txtKode.setText(kondisi.getKodeKondisi());
            txtNama.setText(kondisi.getNamaKondisi());
            txtKode.setEditable(false);
        }

        panel.add(lblKode);
        panel.add(txtKode);
        panel.add(lblNama);
        panel.add(txtNama);

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

                if (kode.isEmpty() || nama.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            dialog,
                            "Kode dan nama harus diisi",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                Kondisi newKondisi = new Kondisi(kode, nama);

                if (kondisi == null) {
                    // Add new
                    if (controller.create(newKondisi)) {
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
                    if (controller.update(newKondisi)) {
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

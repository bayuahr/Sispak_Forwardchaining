package views;

import config.Database;
import controllers.PenilaianController;
import models.Penilaian;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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


public class PenilaianPanel extends JPanel {
    private PenilaianController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public PenilaianPanel() {
        controller = new PenilaianController();
        initComponents();
        loadData();
    }
    
    private void printReport() {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            // Path file jrxml dan jasper
            String jrxmlPath = "./src/reports/reportpenilaian.jrxml";
            String jasperPath = "./src/reports/reportpenilaian.jasper";

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
        JLabel lblTitle = new JLabel("Riwayat Penilaian Aset", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitle, BorderLayout.NORTH);
        
        // Table
        tableModel = new DefaultTableModel(
            new Object[]{
                "Kode Aset", "Nama Aset", "Tanggal Penilaian", 
                "Hasil", "Keterangan"
            }, 0
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
        
        JButton btnRefresh = new JButton("Refresh");
        JButton btnCetak = new JButton("Cetak Laporan");
        
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnCetak);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Action Listeners
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });
        
        btnCetak.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cetakLaporan();
            }
        });
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Penilaian> penilaianList = controller.getAllWithDetails();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        
        for (Penilaian penilaian : penilaianList) {
            tableModel.addRow(new Object[]{
                penilaian.getKodeAset(),
                penilaian.getNamaAset(),
                sdf.format(penilaian.getTanggalPenilaian()),
                penilaian.getHasil(),
                penilaian.getKeterangan()
            });
        }
    }
    
    private void cetakLaporan() {
        // Implementasi cetak laporan bisa menggunakan JasperReports atau lainnya
        JOptionPane.showMessageDialog(
            this, 
            "Fitur cetak laporan akan diimplementasikan", 
            "Info", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
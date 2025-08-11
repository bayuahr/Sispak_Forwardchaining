package views;

import javax.swing.*;
import java.awt.*;

public class LaporanPanel extends JPanel {
    public LaporanPanel() {
        setLayout(new BorderLayout());
        
        JLabel lblTitle = new JLabel("Laporan Penilaian Aset", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitle, BorderLayout.CENTER);
    }
}
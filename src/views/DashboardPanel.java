package views;

import controllers.AsetController;
import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        AsetController aset = new AsetController();
        int jumlahAset = aset.getCount();
        setLayout(new BorderLayout());

        // Judul
        JLabel lblTitle = new JLabel("Dashboard", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        // Panel konten
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // biar card bisa berdampingan
        contentPanel.setBackground(Color.WHITE);

        // Card jumlah aset
        JPanel asetCard = new JPanel();
        asetCard.setLayout(new BoxLayout(asetCard, BoxLayout.Y_AXIS));
        asetCard.setBackground(Color.WHITE);
        asetCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true), // border rounded
                BorderFactory.createEmptyBorder(20, 30, 20, 30) // padding
        ));

        JLabel lblAsetTitle = new JLabel("Jumlah Aset");
        lblAsetTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblAsetTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblAsetValue = new JLabel(String.valueOf(jumlahAset));
        lblAsetValue.setFont(new Font("Arial", Font.BOLD, 24));
        lblAsetValue.setForeground(new Color(0, 123, 255)); // warna biru biar menonjol
        lblAsetValue.setAlignmentX(Component.CENTER_ALIGNMENT);

        asetCard.add(lblAsetTitle);
        asetCard.add(Box.createRigidArea(new Dimension(0, 10)));
        asetCard.add(lblAsetValue);

        contentPanel.add(asetCard);

        add(contentPanel, BorderLayout.CENTER);
    }
}

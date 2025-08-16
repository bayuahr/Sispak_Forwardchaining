package views;

import models.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainForm extends JFrame {

    private User loggedInUser;
    private JPanel contentPanel;

    public MainForm(User user) {
        this.loggedInUser = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistem Penilaian Aset BMN - Logged in as " + loggedInUser.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Sidebar
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(51, 51, 51));
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));

        // Tambahkan logo di pojok kiri atas
        JLabel logoLabel = new JLabel();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        java.net.URL imgURL = getClass().getResource("/image/logo_bmn.png");
        if (imgURL != null) {
            ImageIcon logoIcon = new ImageIcon(imgURL);
            Image img = logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(img));
        } else {
            System.out.println("Logo tidak ditemukan!");
            logoLabel.setText("LOGO");
            logoLabel.setForeground(Color.WHITE);
        }
        logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidebarPanel.add(logoLabel);

        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20))); // jarak antara logo dan menu

        // Sidebar items (tanpa "Laporan")
        String[] menuItems = {
            "Dashboard",
            "Master Jenis Aset",
            "Master Kondisi",
            "Master Aset",
            "Master Aturan",
            "Penilaian Aset",
            "Logout"
        };

        for (String item : menuItems) {
            JButton btn = new JButton(item);
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setMaximumSize(new Dimension(200, 40));
            btn.setBackground(new Color(51, 51, 51));
            btn.setForeground(Color.WHITE);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);

            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switchPanel(item);
                }
            });

            sidebarPanel.add(btn);
        }

        // Content area
        contentPanel = new JPanel(new CardLayout());
        contentPanel.add(new DashboardPanel(), "Dashboard");
        contentPanel.add(new JenisAsetPanel(), "Master Jenis Aset");
        contentPanel.add(new KondisiPanel(), "Master Kondisi");
        contentPanel.add(new AsetPanel(), "Master Aset");
        contentPanel.add(new AturanPanel(), "Master Aturan");
        contentPanel.add(new PenilaianPanel(), "Penilaian Aset");
        // contentPanel.add(new LaporanPanel(), "Laporan"); // dihapus

        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void switchPanel(String panelName) {
        if (panelName.equals("Logout")) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Yakin ingin logout?",
                    "Konfirmasi Logout",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginForm().setVisible(true);
            }
            return;
        }

        CardLayout cl = (CardLayout) (contentPanel.getLayout());
        cl.show(contentPanel, panelName);
    }
}

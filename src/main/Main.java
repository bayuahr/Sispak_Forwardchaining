package main;

import javax.swing.UIManager;
import views.LoginForm;

public class Main {

    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Show login form
        LoginForm loginForm = new LoginForm();
        loginForm.setVisible(true);
    }
}

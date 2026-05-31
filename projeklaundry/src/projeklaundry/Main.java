package projeklaundry;

import javax.swing.*;
import view.LoginView;

public class Main {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            
        }

        UIManager.put("defaultFont", new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginView().setVisible(true);
            }
        });
    }
}
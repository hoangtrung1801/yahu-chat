package main;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Login extends JFrame {

    private JPanel panel;

    private JLabel lTitle = new JLabel("Title");
    private JLabel lUsername = new JLabel("Username");
    private JLabel lPassword = new JLabel("Password");

    private JTextField tUsername = new JTextField();
    private JTextField tPassword = new JTextField();

    public Login() {
        SwingUtilities.invokeLater(() -> {
            FlatLightLaf.setup();
            createGUI();
        });
    }

    void createGUI() {
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 10, 20, 10));

        c.gridwidth = GridBagConstraints.REMAINDER;
        grid.setConstraints(lTitle, c);
        panel.add(lTitle);

        c.weightx = 1.0;
        grid.setConstraints(lUsername, c);
        panel.add(lUsername);

//        panel.add(tUsername);
//
//        panel.add(lPassword);
//        panel.add(tPassword);

        setContentPane(panel);

        setSize(300, 400);
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        new Login();
    }
}

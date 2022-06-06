package main.client;

import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.layout.AC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {

    int WIDTH = 300;
    int HEIGHT = 400;

    JFrame frame;
    JPanel panel;
    JLabel lEmail, lPassword, lTitle;
    JTextField tEmail, tPassword;
    JButton bLogin;

    public Login() {
        frame = new JFrame();
        panel = new JPanel();
        panel.setLayout(new MigLayout(
                "align center, insets 0 20 0 20"
        ));

        lTitle = new JLabel("Login");
        lTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lTitle.setHorizontalAlignment(JLabel.CENTER);
        lTitle.setPreferredSize(new Dimension(WIDTH, 40));
        panel.add(lTitle, "span, wrap 10px");

        lEmail = new JLabel("Email: ");
        tEmail = new JTextField();

        panel.add(lEmail, "width 30%, alignx right");
        panel.add(tEmail, "width 70%, wrap 10px");

        lPassword = new JLabel("Password: ");
        tPassword = new JTextField();

        panel.add(lPassword, "width 30%, alignx right");
        panel.add(tPassword, "width 70%, wrap 10px");

        bLogin = new JButton("Login");
        bLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        panel.add(bLogin, "span, align center, wrap");

        frame.setContentPane(panel);

        frame.setVisible(true);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
    }

    void login() {
        String email = tEmail.getText();
        String password = tPassword.getText();

        loginSuccess();
    }

    void loginSuccess() {
        frame.dispose();
        new Thread(new Client()).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatLightLaf.setup();
            new Login();
        });
    }
}

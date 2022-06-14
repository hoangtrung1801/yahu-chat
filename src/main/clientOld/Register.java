package main.clientOld;

import com.formdev.flatlaf.FlatLightLaf;
import main.model.User;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register {

    int WIDTH = 300;
    int HEIGHT = 400;

    JFrame frame;
    JPanel panel;
    JLabel lUsername, lPassword, lConfirmPassword, lTitle;
    JTextField tUsername, tPassword, tConfirmPassword;
    JButton bRegister;

    public Register() {
        frame = new JFrame();
        panel = new JPanel();
        panel.setLayout(new MigLayout(
                "align center, insets 0 20 0 20"
        ));

        lTitle = new JLabel("REGISTER");
        lTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lTitle.setHorizontalAlignment(JLabel.CENTER);
        lTitle.setPreferredSize(new Dimension(WIDTH, 40));
        panel.add(lTitle, "span, wrap 10px");

        lUsername = new JLabel("Username: ");
        tUsername = new JTextField();

        panel.add(lUsername, "width 30%, alignx left");
        panel.add(tUsername, "width 70%, wrap 10px");

        lPassword = new JLabel("Password: ");
        tPassword = new JPasswordField();

        panel.add(lPassword, "width 30%, alignx left");
        panel.add(tPassword, "width 70%, wrap 10px");

        lConfirmPassword = new JLabel("Confirm password: ");
        tConfirmPassword = new JPasswordField();

        panel.add(lConfirmPassword, "width 30%, alignx left");
        panel.add(tConfirmPassword, "width 70%, wrap 10px");

        bRegister = new JButton("Register");
        bRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });
        panel.add(bRegister, "span, align center, wrap");

        frame.setContentPane(panel);

        frame.setVisible(true);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    void register() {
        String username = tUsername.getText();
        String password = tPassword.getText();
        String confirmPassword = tConfirmPassword.getText();

        // validate username, password
        if(username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            System.out.println("Please type full information");
            return;
        }

        // validate if have same username in database
        if(User.getUserWithUsername(username) != null)  {
            System.out.println("Username is used");
            return;
        }

        // create new user
        if(password.equals(confirmPassword)) {
            if(User.newUser(username, password)) {
                registerSuccess();
            }
        } else {
            System.out.println("Password and confirm password is not the same");
        }
    }

    void registerSuccess() {
        JOptionPane.showMessageDialog(frame, "You registered successfull");
        frame.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatLightLaf.setup();
            new Register();
        });
    }
}

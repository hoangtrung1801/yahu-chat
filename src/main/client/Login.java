package main.client;

import com.formdev.flatlaf.FlatLightLaf;
import main.model.User;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login {

    int WIDTH = 300;
    int HEIGHT = 400;

    public JFrame frame;
    JPanel panel;
    JLabel lUsername, lPassword, lTitle, lRegisterButton;
    JTextField tUsername, tPassword;
    JButton bLogin;

    ChatClient client;

    public Login(ChatClient client) {
        this.client = client;

        frame = new JFrame();
        panel = new JPanel();
        panel.setLayout(new MigLayout(
                "align center, insets 0 20 0 20"
        ));

        // title
        lTitle = new JLabel("LOGIN");
        lTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lTitle.setHorizontalAlignment(JLabel.CENTER);
        lTitle.setPreferredSize(new Dimension(WIDTH, 40));
        panel.add(lTitle, "span, wrap 10px");

        // username field
        lUsername = new JLabel("Username: ");
        tUsername = new JTextField();

        panel.add(lUsername, "width 30%, alignx right");
        panel.add(tUsername, "width 70%, wrap 10px");

        // password field
        lPassword = new JLabel("Password: ");
        tPassword = new JPasswordField();
        tPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });

        panel.add(lPassword, "width 30%, alignx right");
        panel.add(tPassword, "width 70%, wrap 10px");

        // login button
        bLogin = new JButton("Login");
        bLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        panel.add(bLogin, "span, align center, wrap 16px");

        lRegisterButton = new JLabel("You have not registered");
        lRegisterButton.setForeground(Color.blue.darker());
        lRegisterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lRegisterButton.setText("<html><a href='#'>You have not register ?</a></html>");
        lRegisterButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                showRegisterGUI();
            }
        });
        panel.add(lRegisterButton, "span, align center, wrap");

        frame.setContentPane(panel);

        frame.setVisible(true);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    void login() {
        String username = tUsername.getText();
        String password = tPassword.getText();

        if(User.validateUser(username, password)) {
            ApplicationContext.setUser(User.getUserWithUsername(username));
            loginSuccess();
        } else {
            System.out.println("Username or password is incorrect");
        }
    }

    void loginSuccess() {
        frame.dispose();
        client.loginSuccess();
//        new Thread(new Client()).start();
    }

    void showRegisterGUI() {
        Register register = new Register();
        register.show();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatLightLaf.setup();
            new Login(null);
        });
    }
}

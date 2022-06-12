package main.clientOld;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class ClientControl {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatLightLaf.setup();
            new Login();
        });
    }
}

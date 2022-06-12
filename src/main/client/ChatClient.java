package main.client;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class ChatClient {

    public static void main(String[] args) {
        new ChatClient();
    }

    private String clientName;

    public ClientGUI clientGUI;
    public ClientConnection clientConnection;

    public ChatClient() {
//        FlatLightLaf.setup();
//        SwingUtilities.invokeLater(() -> {
//            new Login(this);
//        });

        init();
    }

    public void loginSuccess() {
        init();
    }

    // -------------------------- Initialize --------------------------
    private void init() {
        initGUI();
        initSocket();
    }

    private void initGUI() {
        clientGUI = new ClientGUI(this);
    }

    private void initSocket() {
        clientConnection = new ClientConnection(this);
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}

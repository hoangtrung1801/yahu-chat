package main.client;

import com.formdev.flatlaf.FlatLightLaf;
import main.model.User;

import javax.swing.*;

public class ChatClient {

    public static void main(String[] args) {
        new ChatClient();
    }

    private String clientName;

    public User user;
    public ClientGUI clientGUI;
    public ClientConnection clientConnection;

    public ChatClient() {
        SwingUtilities.invokeLater(() -> {
            FlatLightLaf.setup();
            new Login(this);
        });
    }

    public void loginSuccess() {
        setUser(ApplicationContext.getUser());
        init();
    }

    // -------------------------- Initialize --------------------------
    private void init() {
        initGUI();
        initSocket();
    }

    private void initGUI() {
        clientGUI = new ClientGUI(this);
        ApplicationContext.setClientGUI(clientGUI);
    }

    private void initSocket() {
        clientConnection = new ClientConnection(this);
        ApplicationContext.setClientConnection(clientConnection);
    }

    public void closeClient() {
        clientConnection.close();
    }

    // ---------------------------------------------------------------
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

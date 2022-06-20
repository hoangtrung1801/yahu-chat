package client;

import com.formdev.flatlaf.FlatLightLaf;
import dao.implement.UserDAOImpl;
import model.User;

import javax.swing.*;

public class ChatClient {

    public static void main(String[] args) {
        new ChatClient();
    }

    private String clientName;

    public ChatClient() {
        ApplicationContext.setChatClient(this);
        SwingUtilities.invokeLater(() -> {
            FlatLightLaf.setup();
            new Login(this);
        });
    }

    // -------------------------- Initialize --------------------------
    private void init() {
        initGUI();
        initSocket();

        ApplicationContext.setUserDAO(new UserDAOImpl());
    }

    private void initGUI() {
        new ClientGUI(this);
    }

    private void initSocket() {
        new ClientConnection();
    }

    public void closeClient() {
        ApplicationContext.getClientConnection().close();
    }

    // ----------- Action ---------
    public void loginSuccess() {
        init();
    }
}

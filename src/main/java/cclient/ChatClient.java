package cclient;

import com.formdev.flatlaf.FlatLightLaf;
import model.User;

public class ChatClient {

    public static User user;
    public static ClientConnection connection;
    public static ClientGUI clientGUI;

    public ChatClient() {
        login();
    }

    private void login() {
        new Login(this);
    }

    public void loginSuccess() {

        // when logging successfully
        clientGUI = new ClientGUI();

        connection = new ClientConnection();
        new Thread(connection).start();

    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        new ChatClient();
    }
}

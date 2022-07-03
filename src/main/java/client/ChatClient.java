package client;

import client.emojipicker.OpenMojiFont;
import com.formdev.flatlaf.FlatLightLaf;
import model.User;

import java.awt.*;
import java.io.IOException;

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

    public static void main(String[] args) throws IOException, FontFormatException {
        OpenMojiFont.load();
        FlatLightLaf.setup();
        new ChatClient();

    }
}

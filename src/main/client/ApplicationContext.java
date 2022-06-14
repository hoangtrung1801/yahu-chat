package main.client;

import main.model.User;

public class ApplicationContext {

    private static User user;
    private static ClientGUI clientGUI;
    private static ClientConnection clientConnection;

    public static void setUser(User user) {
        ApplicationContext.user = user;
    }

    public static User getUser() {
        return user;
    }

    public static ClientGUI getClientGUI() {
        return clientGUI;
    }

    public static void setClientGUI(ClientGUI clientGUI) {
        ApplicationContext.clientGUI = clientGUI;
    }

    public static ClientConnection getClientConnection() {
        return clientConnection;
    }

    public static void setClientConnection(ClientConnection clientConnection) {
        ApplicationContext.clientConnection = clientConnection;
    }
}
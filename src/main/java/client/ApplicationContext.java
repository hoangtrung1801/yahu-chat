package client;

import dao.UserDAO;
import dao.implement.UserDAOImpl;
import model.User;

public class ApplicationContext {

    private static User user;
    private static ClientGUI clientGUI;
    private static ClientConnection clientConnection;
    private static UserDAO userDAO;
    private static ChatClient chatClient;

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

    public static UserDAO getUserDAO() {
        return userDAO;
    }

    public static void setUserDAO(UserDAO userDAO) {
        ApplicationContext.userDAO = userDAO;
    }

    public static ChatClient getChatClient() {
        return chatClient;
    }

    public static void setChatClient(ChatClient chatClient) {
        ApplicationContext.chatClient = chatClient;
    }
}
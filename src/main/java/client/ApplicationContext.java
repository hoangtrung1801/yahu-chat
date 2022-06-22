package client;

import dao.ConversationDAO;
import dao.UserDAO;
import dao.implement.ConversationDAOImpl;
import dao.implement.UserDAOImpl;
import model.User;

public class ApplicationContext {

    private static User user;
    private static ClientGUI clientGUI;
    private static ClientConnection clientConnection;
    private static ChatClient chatClient;

    private static UserDAO userDAO;
    private static ConversationDAO conversationDAO;

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
        if(userDAO == null) userDAO = new UserDAOImpl();
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

    public static ConversationDAO getConversationDAO() {
        if(conversationDAO == null) conversationDAO = new ConversationDAOImpl();
        return conversationDAO;
    }

    public static void setConversationDAO(ConversationDAO conversationDAO) {
        ApplicationContext.conversationDAO = conversationDAO;
    }
}
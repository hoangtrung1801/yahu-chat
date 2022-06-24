package server;

import dao.ConversationDAO;
import dao.UserDAO;
import dao.implement.ConversationDAOImpl;
import dao.implement.UserDAOImpl;
import model.Conversation;
import model.GroupMember;
import model.User;
import utilities.Constants;
import utilities.Helper;
import utilities.SocketHandlerBase;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ServerConnection extends SocketHandlerBase implements Runnable {

    private String clientName;
    private User user;

    UserDAO userDAO;
    ConversationDAO conversationDAO;

    public ServerConnection(Socket socket) {
        super(socket);

        userDAO = new UserDAOImpl();
        conversationDAO = new ConversationDAOImpl();
    }

    @Override
    public void run() {
        boolean isRunning = true;
        while(isRunning) {
            try {
                // wait for receiving data from client
                String received = din.readUTF();

                System.out.println("RECEIVED : " + received);

                String type = Helper.getReceivedType(received);
                if(type.equals(Constants.NOTIFY_USER_ENTERED)) {
                    onUserEntered(received);
                } else if(type.equals(Constants.TEXT_MESSAGE_EVENT)) {
                    onReceiveTextMessage(received);
                } else if(type.equals(Constants.FILE_MESSAGE_EVENT)) {
//                        onReceiveFileMessage(received);
                } else if(type.equals(Constants.TEST)) {
                    sendData(received.toUpperCase());
                }
            } catch (Exception e) {
                isRunning = false;
                System.out.println("Client existed");
            }
        }

        ChatServer.connectionPool.remove(this);
        ChatServer.connectionPool.broadcastOnlineUsers();
        close();
    }

    private void onUserEntered(String received) {
        /*
            NOTIFY_USER_ENTERED;userID
         */
        // unpack client data
        ArrayList<String> clientData = Helper.unpack(received);
        String userID = clientData.get(1);
        user = userDAO.readById(Integer.parseInt(userID));

        // broadcast new client entered
        ChatServer.connectionPool.broadcastOnlineUsers();
    }

    private void onReceiveTextMessage(String received) {
        // unpack received string
        ArrayList<String> messageData = Helper.unpack(received);

        System.out.println("Begin get conversation");
        int conversationId = Integer.parseInt(messageData.get(2));
        Conversation conversation = conversationDAO.readById(conversationId);

        System.out.println(conversation.getConversationName());
        System.out.println("Done");

        // test
//        ChatServer.connectionPool.broadcastOnlineUsers();

//        for(GroupMember gm : conversation.getGroupMembers()) {
//            // check if user in connection pool was in conversation
//            if(gm.getConversation().getId() == conversationId) {
//                ServerConnection con = ChatServer.connectionPool.findWithUser(gm.getUser());
//                con.sendData(received);
//            }
//        }

        // process save message
//        Conversation conversation = conversationDAO.reajd
    }

    private void onReceiveFileMessage(String received) {
        try {
            ArrayList<String> data = Helper.unpack(received);
            System.out.println("Sender : " + data.get(1));

            long size = din.readLong();
            System.out.println("Size : " + size);
            int count;
            byte[] buffer = new byte[8192];
            while(size > 0 && (count = din.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
                size -= count;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

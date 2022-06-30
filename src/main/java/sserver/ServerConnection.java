package sserver;

import com.sun.jdi.event.ExceptionEvent;
import dao.ConversationDAO;
import dao.MessageDAO;
import dao.UserDAO;
import dao.implement.ConversationDAOImpl;
import dao.implement.MessageDAOImpl;
import dao.implement.UserDAOImpl;
import model.Conversation;
import model.GroupMember;
import model.Message;
import model.User;
import shared.ConnectionBase;
import utilities.Constants;
import utilities.Helper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.Socket;
import java.time.Instant;
import java.util.List;
import java.util.Set;

public class ServerConnection extends ConnectionBase implements Runnable {

    public User user;
    private final UserDAO userDAO;
    private final ConversationDAO conversationDAO;
    private final MessageDAO messageDAO;

    public ServerConnection(Socket socket) {
        super(socket);

        userDAO = new UserDAOImpl();
        conversationDAO = new ConversationDAOImpl();
        messageDAO = new MessageDAOImpl();
    }

    @Override
    public synchronized void run() {
        boolean isRunning = true;
        while(isRunning) {
            try {
                System.out.println("RECEIVING... ");
                String data = din.readUTF();
                System.out.println("RECEIVED:  " + data);

                String type = Helper.getReceivedType(data);
                if(type.equals(Constants.ONLINE_USERS_EVENT)) {
                    userEnteredEvent(data);
                } else if(type.equals(Constants.TEXT_MESSAGE_EVENT)) {
                    textMesageEvent(data);
                } else if(type.equals(Constants.IMAGE_MESSAGE_EVENT)) {
//                    imageMessageEvent(data);
                    System.out.println("image message event");
                    List<String> dataAr = Helper.unpack(data);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    int size = Integer.parseInt(dataAr.get(3));
                    int bytes;
                    byte[] buffer = new byte[4 * 1024];

                    while (size > 0 && (bytes = din.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                        baos.write(buffer, 0, bytes);
                        size -= bytes;
                    }

                    imageMessageEvent(data, baos);
                }
            } catch (Exception e) {
                e.printStackTrace();
                isRunning = false;
                System.out.println("CLIENT EXISTED");
            }
        }

        ChatServer.connectionManager.remove(this);
        close();
    }

    // --------------- EVENT -----------------
    public void userEnteredEvent(String data) {
        List<String> dataAr = Helper.unpack(data);
        int id = Integer.parseInt(dataAr.get(1));
        user = userDAO.readById(id);
        ChatServer.sendOnlineUsersEvent();
    }

    public void textMesageEvent(String data) {
        List<String> dataAr = Helper.unpack(data);
        int conversationId = Integer.parseInt(dataAr.get(1));
        int userId = Integer.parseInt(dataAr.get(2));
        String textMessage = dataAr.get(3);

        Conversation conversation = conversationDAO.readById(conversationId);
        Message message = new Message(textMessage, Instant.now());
        message.setConversation(conversation);
        if(user.getId() == userId) message.setUser(user);
        messageDAO.create(message);

        // find user in conversation and send it
        Set<GroupMember> gms = conversation.getGroupMembers();
        for(GroupMember gm: gms) {
            User user = gm.getUser();
            ChatServer.connectionManager.findWithUser(user).sendData(data);
        }
    }

    public void imageMessageEvent(String data, ByteArrayOutputStream baos) {
        try {
            List<String> dataAr = Helper.unpack(data);
            int conversationId = Integer.parseInt(dataAr.get(1));
            byte[] buffer = baos.toByteArray();

            Conversation conversation = conversationDAO.readById(conversationId);

            Set<GroupMember> gms = conversation.getGroupMembers();
            for(GroupMember gm: gms) {
                User user = gm.getUser();
                ServerConnection userConnection = ChatServer.connectionManager.findWithUser(user);

                // send event
                userConnection.sendData(data);

                // send file
                userConnection.dos.write(baos.toByteArray());
                userConnection.dos.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --------------- ACTION -----------------
}

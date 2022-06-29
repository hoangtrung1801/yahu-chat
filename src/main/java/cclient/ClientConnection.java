package cclient;

import dao.UserDAO;
import dao.implement.UserDAOImpl;
import model.Conversation;
import model.User;
import shared.ConnectionBase;
import utilities.Constants;
import utilities.Helper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ClientConnection extends ConnectionBase implements Runnable {

    public UserDAO userDAO = new UserDAOImpl();

    public ClientConnection() {
        super(Constants.URL, Constants.PORT);
        sendUserEnteredEvent();
    }

    @Override
    public void run() {
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
                    textMessageEvent(data);
                } else if(type.equals(Constants.IMAGE_MESSAGE_EVENT)) {
                    System.out.println("client image message event");
                    List<String> dataAr = Helper.unpack(data);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    int size = Integer.parseInt(dataAr.get(3));
                    int bytes;
                    byte[] buffer = new byte[4 * 1024];

                    while (size > 0 && (bytes = din.read(buffer, 0, Math.min(buffer.length, size))) != -1) {
                        baos.write(buffer, 0, bytes);
                        size -= bytes;
                    }


                    imageMessageEvent(data, baos);
                }
            } catch (Exception e) {
                e.printStackTrace();
                isRunning = false;
            }
        }

        close();
    }

    // --------------- EVENT -----------------
    private void userEnteredEvent(String data) {
        List<String> dataAr = Helper.unpack(data);
        List<User> listOnlineUsers = new ArrayList<>();
        for(int i=1;i<dataAr.size();i++) {
            int id = Integer.parseInt(dataAr.get(i));
            listOnlineUsers.add(userDAO.readById(id));
        }

        ChatClient.clientGUI.updateOnlineUsersPanel(listOnlineUsers);
    }

    private void textMessageEvent(String data) {
        List<String> dataAr = Helper.unpack(data);
        int conversationId = Integer.parseInt(dataAr.get(1));
        int senderId = Integer.parseInt(dataAr.get(2));
        String message = dataAr.get(3);

        ChatGUI chatGUI = ChatClient.clientGUI.controller.findChatGUI(conversationId);
        User senderUser = userDAO.readById(senderId);

        if(chatGUI == null) return;
        chatGUI.controller.showTextMessage(senderUser.getUsername(), message);
    }

    private void imageMessageEvent(String data, ByteArrayOutputStream baos) {
        List<String> dataAr = Helper.unpack(data);
        int conversationId = Integer.parseInt(dataAr.get(1));
        int senderId = Integer.parseInt(dataAr.get(2));

        ChatGUI chatGUI = ChatClient.clientGUI.controller.findChatGUI(conversationId);
        User senderUser = userDAO.readById(senderId);

        if(chatGUI == null) return;
        try {
            chatGUI.appendImage(senderUser.getUsername(), ImageIO.read(new ByteArrayInputStream(baos.toByteArray())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --------------- ACTION -----------------
    public void sendUserEnteredEvent() {
        /*
            ONLINE_USERS_EVENT;userId
         */
        sendData(Helper.pack(Constants.ONLINE_USERS_EVENT, ChatClient.user.getId()+""));
    }

    public void sendMessageInConversation(Conversation conversation, String message) {
        /*
            TEXT_MESSAGE_EVENT;conversationId;userId(sender);message
         */
        String data = Helper.pack(Constants.TEXT_MESSAGE_EVENT, conversation.getId()+"", ChatClient.user.getId()+"", message);
        sendData(data);
    }

    public void sendImageInConversation(Conversation conversation, BufferedImage bufferedImage) {
        /*
            IMAGE_MESSAGE_EVENT;conversationId;userId(sender);sizeFile
            bufferedImage
         */
        try {
            // convert bufferedImage to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", baos);
            ByteArrayInputStream bios = new ByteArrayInputStream(baos.toByteArray());

            // send event
            sendData(Helper.pack(Constants.IMAGE_MESSAGE_EVENT, conversation.getId()+"", ChatClient.user.getId()+"", baos.size()+""));

            // send file
            byte[] buffer = new byte[4 * 1024];
            int len;
            while((len = bios.read(buffer)) != -1) {
                dos.write(buffer, 0 , len);
                dos.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


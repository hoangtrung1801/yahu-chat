package cclient;

import dao.UserDAO;
import dao.implement.UserDAOImpl;
import model.Conversation;
import model.User;
import shared.ConnectionBase;
import utilities.Constants;
import utilities.Helper;

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
        int userId = Integer.parseInt(dataAr.get(2));
        String message = dataAr.get(3);

        ChatGUI chatGUI = ChatClient.clientGUI.controller.findChatGUI(conversationId);
        User senderUser = userDAO.readById(userId);

        if(chatGUI == null) return;
        chatGUI.appendTextMessage(senderUser.getUsername(), message);
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
}


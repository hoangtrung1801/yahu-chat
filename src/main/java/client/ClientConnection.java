package client;

import model.Conversation;
import model.User;
import utilities.Constants;
import utilities.Helper;
import utilities.SocketHandlerBase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientConnection extends SocketHandlerBase implements Runnable {

    Thread listener;
    ArrayList<User> onlineUsers;

    public ClientConnection() {
    }

    @Override
    public void run() {
         ApplicationContext.setClientConnection(this);

        try {
            // connect to server
            socket = new Socket(Constants.URL, Constants.PORT);
            System.out.println("Connected to " + Constants.URL + ":" + Constants.PORT + "...");

            // init input and output stream
            dos = new DataOutputStream(socket.getOutputStream());
            dos.flush();
            din = new DataInputStream(socket.getInputStream());

            // listen
            listener = new Thread(this::listen);
            listener.start();

            notifyUserEntered();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listen() {
        boolean isRunning = true;

        while(isRunning) {
            try {
                System.out.println("Receving .... ");
                String received = din.readUTF();

                System.out.println("RECEIVED: " + received);
                String type = Helper.getReceivedType(received);
                if (type.equals(Constants.TEXT_MESSAGE_EVENT)) {
                    onReceiveTextMessage(received);
                } else if (type.equals(Constants.ONLINE_USERS_EVENT)) {
                    onOnlineUsersEvent(received);
                }
            } catch (Exception e) {
                isRunning = false;
                e.printStackTrace();
            }
        }

        System.out.println("Socket close");
        close();
    }

    // ----------------------- ACTION -------------------
//    public void sendTextMessage(String message) {
//        /*
//            "TEXT_MESSAGE_EVENT;senderName;message"
//         */
//        String textMessage = Helper.pack(Constants.TEXT_MESSAGE_EVENT,
//                ApplicationContext.getUser().getUsername(),
//                message
//        );
//        sendData(textMessage);
//    }

    public void sendTextMessage(Conversation conversation, String message) {
        /*
//            "TEXT_MESSAGE_EVENT;senderId;receiverId;message"
            "TEXT_MESSAGE_EVENT;conversationId;message"
         */
//        String textMessage = Helper.pack(Constants.TEXT_MESSAGE_EVENT,
//                ApplicationContext.getUser().getId()+"",
//                targetUser.getId()+"",
//                message
//        );
        String textMessage = Helper.pack(
                Constants.TEXT_MESSAGE_EVENT,
                conversation.getId()+"",
                message
        );
        sendData(textMessage);

        // save on database
    }

    public void sendFileMessage(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            int count;
            byte[] buffer = new byte[8192];

            String fileMesasge = Helper.pack(Constants.FILE_MESSAGE_EVENT, ApplicationContext.getUser().getUsername());
            sendData(fileMesasge);
            dos.writeLong(file.length());
            while((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);
                dos.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void notifyUserEntered() {
        /*
            NOTIFY_USER_ENTERED;userID;
         */
        User user = ApplicationContext.getUser();
        String data = Helper.pack(Constants.NOTIFY_USER_ENTERED, user.getId()+"");

        sendData(data);
    }


    // ----------------------- LISTENER -------------------
    private void onReceiveTextMessage(String received) {
//        ArrayList<String> data = Helper.unpack(received);
//        int senderID = Integer.parseInt(data.get(1));
//        int receiverID = Integer.parseInt(data.get(2));
//        String message = data.get(3);
//
//        User sender = ApplicationContext.getUserDAO().readById(senderID);
////        User receiver = ApplicationContext.getUserDAO().readById(receiverID);
//
//        for(ChatGUI chatGUI: ApplicationContext.getClientGUI().chatManager) {
//            if(chatGUI.controller.getTargetUser().getId() == senderID || chatGUI.controller.getTargetUser().getId() == receiverID
//                    || ApplicationContext.getUser().getId() == senderID || ApplicationContext.getUser().getId() == receiverID
//            ) {
//                chatGUI.appendTextMessage(sender.getUsername(), message);
//            }
//        }
    }

    private void onOnlineUsersEvent(String received) {
        ArrayList<String> data = Helper.unpack(received);
        ArrayList<User> list = new ArrayList<>();
        for (String userID : data.subList(1, data.size())) {
            User user = ApplicationContext.getUserDAO().readById(Integer.parseInt(userID));
            list.add(user);
        }

        onlineUsers = list;
        ApplicationContext.getClientGUI().updateOnlineUsersPanel();
    }
}

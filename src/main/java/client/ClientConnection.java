package client;

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

public class ClientConnection extends SocketHandlerBase {

    Thread listener;
    ArrayList<User> onlineUsers;

    public ClientConnection() {
        ApplicationContext.setClientConnection(this);

        try {
            // connect to server
            socket = new Socket(Constants.URL, Constants.PORT);
            System.out.println("Connected to " + Constants.URL + ":" + Constants.PORT + "...");

            // init input and output stream
            din = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

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
                if (type.equals(Constants.NOTIFY_USER_ENTERED)) {
                    onUserEntered(received);
                } else if (type.equals(Constants.TEXT_MESSAGE_EVENT)) {
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

    public void sendTextMessage(User targetUser, String message) {
        /*
            "TEXT_MESSAGE_EVENT;senderId;receiverId;message"
         */
        String textMessage = Helper.pack(Constants.TEXT_MESSAGE_EVENT,
                ApplicationContext.getUser().getId()+"",
                targetUser.getId()+"",
                message
        );
        sendData(textMessage);
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
    private void onUserEntered(String received) {
        ArrayList<String> data = Helper.unpack(received);
        String senderName = data.get(1);

//        ApplicationContext.getClientGUI().app
//        client.clientGUI.appendTextUserEntered(senderName);
    }

    private void onReceiveTextMessage(String received) {
        ArrayList<String> data = Helper.unpack(received);
        int senderID = Integer.parseInt(data.get(1));
        int receiverID = Integer.parseInt(data.get(2));
        String message = data.get(3);

        User sender = ApplicationContext.getUserDAO().readById(senderID);
        User receiver = ApplicationContext.getUserDAO().readById(receiverID);

        for(ChatGUI chatGUI: ApplicationContext.getClientGUI().chatManager) {
            if(chatGUI.targetUser.getId() == receiverID) {
                chatGUI.appendTextMessage(ApplicationContext.getUser().getUsername(), message);
            }
        }
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

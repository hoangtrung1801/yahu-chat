package main.client;

import main.model.User;
import main.utilities.Constants;
import main.utilities.Helper;
import main.utilities.SocketHandlerBase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientConnection extends SocketHandlerBase {

    ChatClient client;
    Thread listener;

    ArrayList<User> onlineUsers;

    public ClientConnection(ChatClient client) {

        this.client = client;
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
    public void sendTextMessage(String message) {
        /*
            "TEXT_MESSAGE_EVENT;senderName;message"
         */
        String textMessage = Helper.pack(Constants.TEXT_MESSAGE_EVENT, client.getClientName(), message);
        sendData(textMessage);
    }

    public void sendTextMessage(ChatGUI chatGUI, String message) {
        String textMessage = Helper.pack(Constants.TEXT_MESSAGE_EVENT, String.valueOf(client.getUser().getUserId()), message);
        sendData(textMessage);
    }

    public void sendFileMessage(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            int count;
            byte[] buffer = new byte[8192];

            String fileMesasge = Helper.pack(Constants.FILE_MESSAGE_EVENT, client.getClientName());
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
        String data = Helper.pack(Constants.NOTIFY_USER_ENTERED, user.getUserId()+"");

        sendData(data);
    }


    // ----------------------- LISTENER -------------------
    private void onUserEntered(String received) {
        ArrayList<String> data = Helper.unpack(received);
        String senderName = data.get(1);

//        client.clientGUI.appendTextUserEntered(senderName);
    }

    private void onReceiveTextMessage(String received) {
        ArrayList<String> data = Helper.unpack(received);
        String senderName = data.get(1);
        String textMessage = data.get(2);

//        client.clientGUI.appendTextMessage(senderName, textMessage);
    }

    private void onOnlineUsersEvent(String received) {
        ArrayList<String> data = Helper.unpack(received);
        ArrayList<User> list = new ArrayList<>();
        for (String userID : data.subList(1, data.size())) {
            User userWithID = User.getUserWithID(Integer.parseInt(userID));
            list.add(userWithID);
        }

        onlineUsers = list;
        ApplicationContext.getClientGUI().updateOnlineUsersPanel();
    }
}

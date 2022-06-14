package main.server;

import main.model.User;
import main.utilities.Constants;
import main.utilities.Helper;
import main.utilities.SocketHandlerBase;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ServerConnection extends SocketHandlerBase implements Runnable {

    public String clientName;
    public User user;

    public ServerConnection(Socket socket) {
        super(socket);
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
                }
//                    if (clientName == null) {
//                        onReceiveClientData(received);
//                    } else {
//                        String type = Helper.getReceivedType(received);
//
//                        if (type.equals(Constants.TEXT_MESSAGE_EVENT)) {
//                            onReceiveTextMessage(received);
//                        }
//                    }
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
        user = User.getUserWithID(Integer.parseInt(userID));

        // broadcast new client entered
//        sendData(received);
//        broadcast(received);
        ChatServer.connectionPool.broadcastOnlineUsers();
    }

    private void onReceiveTextMessage(String received) {
        // unpack received string
        ArrayList<String> messageData = Helper.unpack(received);
        String senderName = messageData.get(1);
//        String receiverName = messageData.get(2);
//        String textMessage = messageData.get(3);
        String textMessage = messageData.get(2);

        // send message data to receiver and sender
//        ServerConnection sender = ChatServer.connectionPool.findWithName(senderName);
//        sender.sendData(received);
//
//        ServerConnection receiver = ChatServer.connectionPool.findWithName(receiverName);
//        receiver.sendData(received);

//        sendData(received);
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
}

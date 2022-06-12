package main.server;

import main.utilities.Constants;
import main.utilities.Helper;
import main.utilities.SocketHandlerBase;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ServerConnection extends SocketHandlerBase implements Runnable {

    public String clientName;

    public ServerConnection(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    public void run() {
        boolean isRunning = true;
        while(isRunning) {
            try {
                if(din.available() > 0) {
                    // wait for receiving data from client
                    String received = din.readUTF();

                    System.out.println("RECEIVED : " + received);

                    String type = Helper.getReceivedType(received);
                    if(type.equals(Constants.NOTIFY_USER_ENTERED)) {
                        onUserEntered(received);
                    } else if(type.equals(Constants.TEXT_MESSAGE_EVENT)) {
                        onReceiveTextMessage(received);
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
                }
            } catch (Exception e) {
                isRunning = false;
                e.printStackTrace();
            }
        }
    }

    private void onUserEntered(String received) {
        // unpack client data
        ArrayList<String> clientData = Helper.unpack(received);
        String name = clientData.get(1);

        // save
        clientName = name;

        // broadcast new client entered
        sendData(received);
    }

    private void onReceiveTextMessage(String recevied) {
        // unpack recevied string
        ArrayList<String> messageData = Helper.unpack(recevied);
        String senderName = messageData.get(1);
//        String receiverName = messageData.get(2);
//        String textMessage = messageData.get(3);
        String textMessage = messageData.get(2);

        // send message data to receiver and sender
//        ServerConnection sender = ChatServer.connectionPool.findWithName(senderName);
//        sender.sendData(recevied);
//
//        ServerConnection receiver = ChatServer.connectionPool.findWithName(receiverName);
//        receiver.sendData(recevied);

        sendData(recevied);
    }
}

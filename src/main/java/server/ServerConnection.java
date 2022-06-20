package server;

import dao.UserDAO;
import dao.implement.UserDAOImpl;
import model.User;
import utilities.Constants;
import utilities.Helper;
import utilities.SocketHandlerBase;

import java.net.Socket;
import java.util.ArrayList;

public class ServerConnection extends SocketHandlerBase implements Runnable {

    public String clientName;
    public User user;

    UserDAO userDAO;

    public ServerConnection(Socket socket) {
        super(socket);
        userDAO = new UserDAOImpl();
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
        int senderID = Integer.parseInt(messageData.get(1));
        int receiverID = Integer.parseInt(messageData.get(2));

        // send message data to receiver and sender
        ServerConnection sender = ChatServer.connectionPool.findWithUserID(senderID);
        sender.sendData(received);

        ServerConnection receiver = ChatServer.connectionPool.findWithUserID(receiverID);
        receiver.sendData(received);
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

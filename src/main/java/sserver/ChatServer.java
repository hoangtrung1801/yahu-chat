package sserver;

import utilities.Constants;
import utilities.Helper;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ChatServer {

    public static ServerSocket server;
    public static ServerConnectionManager connectionManager;

    private boolean isRunning;

    public ChatServer() {
        initServer();
    }

    private void initServer() {
        try {
            server = new ServerSocket(Constants.PORT);
            connectionManager = new ServerConnectionManager();
            isRunning = true;

            System.out.println("Server is opening at port " + Constants.PORT);

            while(isRunning) {
                Socket clientSocket = server.accept();
                ServerConnection connection = new ServerConnection(clientSocket);
                System.out.println("NEW CLITENT ENTERED");

                connectionManager.add(connection);
                new Thread(connection).start();
            }
        } catch (Exception e) {
            isRunning = false;
            e.printStackTrace();
        }
    }

    public static void sendOnlineUsersEvent() {
        /*
            ONLINE_USERS_EVENT;userId1;userId2;...;userIdN
         */
        String data = Helper.pack(
                Constants.ONLINE_USERS_EVENT,
                connectionManager
                        .getManager()
                        .stream().map(sc -> sc.user.getId()+"")
                        .collect(Collectors.toList())
        );
        for(ServerConnection sc: connectionManager.getManager()) {
            sc.sendData(data);
        }
    }

//    public static void sendDataTo

    public static void main(String[] args) {
        new ChatServer();
    }
}

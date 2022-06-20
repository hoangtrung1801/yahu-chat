package server;

import utilities.Constants;

import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {

    public static ServerSocket server;
    public static volatile ServerConnectionPool connectionPool;
    public boolean isRunning;

    public ChatServer() {
        run();
    }

    void run() {
        try {
            server = new ServerSocket(Constants.PORT);
            System.out.println("Server is opening at port " + Constants.PORT );

            isRunning = true;
            connectionPool = new ServerConnectionPool(this);

            while(isRunning) {
                Socket s = server.accept();
                System.out.println("New client connected");

                ServerConnection connection = new ServerConnection(s);
                connectionPool.add(connection);
            }
        } catch (Exception e) {
            isRunning = false;
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ChatServer();
    }
}

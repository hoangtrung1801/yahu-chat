package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

    ServerSocket server;
    List<ClientHandler> clients = new ArrayList<>();
    public static int PORT = 3000;

    public ChatServer() throws IOException {
        server = new ServerSocket(PORT);

        while(true) {
            Socket client = server.accept();

            ClientHandler clientHandler = new ClientHandler(clients.size(), client, this);
            clients.add(clientHandler);
            new Thread(clientHandler).start();
        }
    }

    public void receiveMessage(String message) {
    }

    public void sendMessageToAll(String message) throws IOException {
        for(ClientHandler client : clients) {
            client.sendMessageToClient(message);
        }
    }

    public static void main(String[] args) throws IOException {
        new ChatServer();
    }
}
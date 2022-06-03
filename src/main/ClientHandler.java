package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {

    int id;
    Socket client;
    ChatServer server;
    Scanner scanner;
    DataInputStream in;
    DataOutputStream out;

    public ClientHandler(int id, Socket client, ChatServer server) throws IOException {
        this.id = id;
        this.client = client;
        this.server = server;

        scanner = new Scanner(System.in);
        in = new DataInputStream(client.getInputStream());
        out = new DataOutputStream(client.getOutputStream());
    }

    @Override
    public void run() {
        while(true) {
            try {
                String messageReceived = in.readUTF();
                System.out.println("Client " + id + " : " + messageReceived);

                server.sendMessageToAll(messageReceived);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessageToClient(String message) throws IOException {
        out.writeUTF(message);
        out.flush();
    }

    public void receiveMessage(String message) {

    }

    void showMessageReceived(String message) {

    }
}

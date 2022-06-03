package main;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

    Socket client;
    DataOutputStream out;
    DataInputStream in;
    GUI gui;

    public ChatClient() throws IOException {
        gui = new GUI(this);

        client = new Socket("localhost", ChatServer.PORT);

        in = new DataInputStream(client.getInputStream());
        out = new DataOutputStream(client.getOutputStream());

        while(true) {
            String messageReceived = in.readUTF();
            receiveMessage(messageReceived);
        }
    }

    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
        out.flush();
    }

    public void receiveMessage(String message) {
        gui.showMessageReceived(message);
    }

    public static void main(String[] args) throws IOException {
        new ChatClient();
    }

}

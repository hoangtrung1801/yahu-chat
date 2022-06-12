package main.client;

import main.utilities.Constants;
import main.utilities.Helper;
import main.utilities.SocketHandlerBase;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientConnection extends SocketHandlerBase {

    ChatClient client;
    Thread listener;

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

            notifyEntered();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listen() {
        boolean isRunning = true;

        while(isRunning) {
            try {
                if(din.available() > 0) {
                    String received = din.readUTF();

                    System.out.println("RECEIVED: " + received);
                    String type = Helper.getReceivedType(received);
                    if (type.equals(Constants.NOTIFY_USER_ENTERED)) {
                        onUserEntered(received);
                    } else if (type.equals(Constants.TEXT_MESSAGE_EVENT)) {
                        onReceiveTextMessage(received);
                    }
                }
            } catch (Exception e) {
                isRunning = false;
                e.printStackTrace();
            }
        }

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

    private void notifyEntered() {
        /*
            NOTIFY_USER_ENTERED;clientName;
         */
        String clientName = JOptionPane.showInputDialog("Your name: ");
        client.setClientName(clientName);
        String data = Helper.pack(Constants.NOTIFY_USER_ENTERED, clientName);

        sendData(data);
    }

    // ----------------------- LISTENER -------------------
    private void onUserEntered(String received) {
        ArrayList<String> data = Helper.unpack(received);
        String senderName = data.get(1);

        client.clientGUI.appendTextUserEntered(senderName);
    }

    private void onReceiveTextMessage(String received) {
        ArrayList<String> data = Helper.unpack(received);
        String senderName = data.get(1);
        String textMessage = data.get(2);

        client.clientGUI.appendTextMessage(senderName, textMessage);
    }
}

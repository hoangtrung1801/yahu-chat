package main.server;

import main.model.Message;
import main.model.User;
import main.utils.Constants;

import javax.swing.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server implements Runnable {

    ServerSocket server;
    List<ServerThread> listClients;

    public boolean isRun = true;

    public Server() {
//        init();
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(Constants.PORT);
            System.out.println("Sever start ...");

            listClients = new ArrayList<>();
            while(isRun) {
                Socket socket = server.accept();
                ServerThread client = new ServerThread(socket, this);
                System.out.println("A client entered");

                listClients.add(client);
                new Thread(client).start();
                sendMessageToAllClients(new Message(new User(null, null, null), "A client entered", new Date()));
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToAllClients(Message message) {
        for(ServerThread st : listClients) {
            st.sendMessageToClient(message);
        }
    }

    public static void main(String[] args) {
        new Thread(new Server()).start();
    }


}

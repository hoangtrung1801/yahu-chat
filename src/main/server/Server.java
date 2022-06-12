package main.server;

import main.model.FileMessage;
import main.model.Message;
import main.model.User;
import main.utilities.Constants;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {

    ServerSocket server;
    List<ServerThread> listClients;

    public boolean isRun = true;

    public Server() {
        try {
            server = new ServerSocket(Constants.PORT);
            System.out.println("Sever start ...");
            listClients = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while(isRun) {
                Socket socket = server.accept();
                ServerThread client = new ServerThread(socket, this);
                System.out.println("A client entered");

                listClients.add(client);
                new Thread(client).start();
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToAllClients(Message message) {
        for(ServerThread st : listClients) {
//            st.sendMessageToClient(Constants.SEND_MESSAGE, message);
        }
    }

    public void notifyUseEntered(User user) {
        for(ServerThread st: listClients) {
            st.sendTextToClient(Constants.NOTIFY_USER_ENTERED, user.getUsername() + " entered!");
        }
    }

    public void sendFileMessageToAllClients(FileMessage fileMessage) {
        for(ServerThread st : listClients) {
            st.sendFileMessageToClient(fileMessage);
        }
    }

    public static void main(String[] args) {
        new Thread(new Server()).start();
    }


}

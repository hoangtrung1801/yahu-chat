package main.server;

import main.model.FileMessage;
import main.model.Message;
import main.model.User;
import main.utilities.Constants;

import java.io.*;
import java.net.Socket;
import java.security.spec.ECField;

public class ServerThread implements Runnable {

    Server server;
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;

    boolean isRun = true;

    public ServerThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());

            while(isRun) {
                while(ois.available() == 0) {
                    Thread.sleep(1);
                }

//                String typeMessage = ois.readUTF();
//                if(typeMessage.equals(Constants.SEND_MESSAGE)) {
//                    receiveMessageFromClient();
//                } else if(typeMessage.equals(Constants.NOTIFY_USER_ENTERED)) {
//                    notifyUserEntered();
//                } else if(typeMessage.equals(Constants.SEND_FILE)) {
//                    receiveFileMessageFromClient();
//                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void receiveMessageFromClient() throws IOException, ClassNotFoundException {
        Message message = (Message) ois.readObject();
        server.sendMessageToAllClients(message);
    }

    void receiveFileMessageFromClient() throws IOException, ClassNotFoundException {
        FileMessage fileMessage = (FileMessage) ois.readObject();
        server.sendFileMessageToAllClients(fileMessage);
    }

    void notifyUserEntered() throws IOException, ClassNotFoundException {
        User user = (User) ois.readObject();
        server.notifyUseEntered(user);
    }

    void sendMessageToClient(String typeMessage, Message message){
//        if(typeMessage.equals(Constants.SEND_MESSAGE)) {
//            try {
//                oos.writeUTF(Constants.SEND_MESSAGE);
//                oos.flush();
//
//                oos.writeObject(message);
//                oos.flush();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    void sendFileMessageToClient(FileMessage fileMessage) {
//        try {
//            oos.writeUTF(Constants.SEND_FILE);
//            oos.flush();
//
//            oos.writeObject(fileMessage);
//            oos.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    void sendTextToClient(String type, String text) {
        try {
            oos.writeUTF(type);
            oos.flush();

            oos.writeUTF(text);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

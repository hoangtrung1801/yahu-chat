package main.server;

import main.model.Message;
import main.utils.Constants;

import java.io.*;
import java.net.Socket;

public class ServerThread implements Runnable {

    Server server;
    Socket socket;
//    DataInputStream din;
//    DataOutputStream dout;
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

                String typeMessage = ois.readUTF();
                if(typeMessage.equals(Constants.SEND_MESSAGE)) {
                    receiveMessageFromClient();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void receiveMessageFromClient() throws IOException, ClassNotFoundException {
        Message message = (Message) ois.readObject();
        server.sendMessageToAllClients(message);
    }

    void sendMessageToClient(Message message){
        try {
            oos.writeUTF(Constants.SEND_MESSAGE);
            oos.flush();

            oos.writeObject(message);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

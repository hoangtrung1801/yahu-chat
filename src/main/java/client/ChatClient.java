package client;

import com.formdev.flatlaf.FlatLightLaf;
import model.User;
import utilities.Constants;
import utilities.Helper;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ChatClient {

    public static void main(String[] args) {
//
//        try {
//            Socket socket;
//            DataInputStream din;
//            DataOutputStream dos;
//
//            socket = new Socket(Constants.URL, Constants.PORT);
//            System.out.println("Connected to " + Constants.URL + ":" + Constants.PORT + "...");
//
//            dos = new DataOutputStream(socket.getOutputStream());
//            din = new DataInputStream(socket.getInputStream());
//
//            User user = ApplicationContext.getUserDAO().readById(5);
//            ApplicationContext.setUser(user);
//
//            while(true) {
//                try {
//                    dos.writeUTF(Helper.pack(Constants.TEST, "hello"));
//                    dos.flush();
//
//                    String received = din.readUTF();
//                    System.out.println("RECEIVED : " + received);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    break;
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        new ChatClient();
    }

    private String clientName;

    public ChatClient() {
        ApplicationContext.setChatClient(this);
        SwingUtilities.invokeLater(() -> {
            FlatLightLaf.setup();
            new Login(this);
        });
    }

    // -------------------------- Initialize --------------------------
    private void init() {
        initGUI();
        initSocket();
    }

    private void initGUI() {
        new ClientGUI(this);
    }

    private void initSocket() {
        ClientConnection clientConnection = new ClientConnection();
        new Thread(clientConnection).start();
    }

    public void closeClient() {
        ApplicationContext.getClientConnection().close();
    }

    // ----------- Action ---------
    public void loginSuccess() {
        init();
    }
}

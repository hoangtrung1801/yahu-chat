package cclient;

import model.User;

public class ChatClient {

    public static User user;
    public static ClientConnection connection;
    public static ClientGUI clientGUI;

    public ChatClient() {
        login();
    }

    private void login() {
        new Login(this);
    }

    public void loginSuccess() {

        // when logging successfully
        clientGUI = new ClientGUI();

        connection = new ClientConnection();
        new Thread(connection).start();

    }

    public static void main(String[] args) {
//        new ChatClient().loginSuccess(null);
        new ChatClient();
//        try {
//            Socket socket = new Socket(Constants.URL, Constants.PORT);
//            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
//            DataInputStream din = new DataInputStream(socket.getInputStream());
//
//            dos.writeUTF("Hello");
//
//            socket.close();
//            dos.close();
//            din.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}

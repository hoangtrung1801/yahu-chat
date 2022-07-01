package shared;

import java.io.*;
import java.net.Socket;

public class ConnectionBase {

    protected Socket socket;
    protected ObjectInputStream ois;
    protected ObjectOutputStream oos;

    public ConnectionBase() {

    }

    public ConnectionBase(Socket socket) {
        try {
            this.socket = socket;

            while(!socket.isConnected()) Thread.sleep(1);

            this.oos = new ObjectOutputStream(socket.getOutputStream());
//            oos.flush();
            this.ois = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ConnectionBase(String url, int port) {
        try {
            this.socket = new Socket(url, port);

            while(!socket.isConnected()) Thread.sleep(1);

            this.oos = new ObjectOutputStream(socket.getOutputStream());
//            oos.flush();
            this.ois = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendData(String data) {
        try {
            oos.writeUTF(data);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendObject(Object data) {
        try {
            oos.writeObject(data);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            socket.close();
            ois.close();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package shared;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ConnectionBase {

    protected Socket socket;
    protected DataInputStream din;
    protected DataOutputStream dos;

    public ConnectionBase() {

    }

    public ConnectionBase(Socket socket) {
        try {
            this.socket = socket;

            while(!socket.isConnected()) Thread.sleep(1);

            this.dos = new DataOutputStream(socket.getOutputStream());
            dos.flush();
            this.din = new DataInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ConnectionBase(String url, int port) {
        try {
            this.socket = new Socket(url, port);

            while(!socket.isConnected()) Thread.sleep(1);

            this.dos = new DataOutputStream(socket.getOutputStream());
            dos.flush();
            this.din = new DataInputStream(socket.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendData(String data) {
        try {
            dos.writeUTF(data);
            dos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            socket.close();
            din.close();
            dos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

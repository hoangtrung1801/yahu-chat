package main.utilities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketHandlerBase{

    protected Socket socket;
    protected DataOutputStream dos;
    protected DataInputStream din;

    public SocketHandlerBase() {

    }

    public SocketHandlerBase(Socket socket) {
        try {
            this.socket = socket;

            this.dos = new DataOutputStream(socket.getOutputStream());
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

    public boolean close() {
        try {
            socket.close();
            din.close();
            dos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

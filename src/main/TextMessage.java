package main;

import java.net.Socket;

public class TextMessage {

    private Socket client;
    private String message;

    public TextMessage(Socket client, String message) {
        this.client = client;
        this.message = message;
    }

    public Socket getClient() {
        return client;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package main.model;

import java.io.Serializable;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private User user;
    private String message;
    private Date timestamp;

    public Message(User user, String message, Date timestamp) {
        this.user = user;
        this.message = message;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        if(user == null) {
//            return user.getUsername() + " entered!";
            return "A client entered";
        }

        String timeFormat = new SimpleDateFormat("HH:mm").format(timestamp);
        return "[" + timeFormat + "] " + user.getUsername() + ": " + message + "\n";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

package main.utils;

import main.model.Message;

public class MessageUtil {
    public static String messageToString(Message message) {
        return "[" + message.getTimestamp().toString() + "] " + message.getUser().getEmail() + ": " + message.getMessage() + "\n";

    }
}

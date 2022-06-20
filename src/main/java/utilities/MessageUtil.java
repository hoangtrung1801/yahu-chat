package utilities;

import model.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MessageUtil {

    public static ArrayList<String> getMessageStringComps(Message message) {
        ArrayList<String> result = new ArrayList<>();
//        String timestamp = "[" + new SimpleDateFormat("HH:mm").format(message.getTimestamp()) + "] ";
//
//        result.add(timestamp);
//        result.add(message.getUser().getUsername() + ": ");
//        result.add(message.getMessage() + "\n");
//
        return result;
    }
}

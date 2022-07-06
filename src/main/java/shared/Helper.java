package shared;

import net.bytebuddy.utility.RandomString;
import shared.Constants;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Helper {

    // -------------------------- Stream Data --------------------------
    /*
        "CLIENT_DATA_EVENT;clientName;key1;key2;key3"
        "CHAT_EVENT;senderName;receiverName;chatContent"
        "ONLINE_LIST_EVENT;userID1;userID2;....;userIDn"
     */
    // https://viettuts.vn/java-new-features/varargs-trong-java
    public static String pack(String... data) {
//        return eventName + Constants.SEPARATE_MARKER + String.join(Constants.SEPARATE_MARKER, data);
        return String.join(Constants.SEPARATE_MARKER, data);
    }

    public static String pack(List<String> data) {
//        return eventName + Constants.SEPARATE_MARKER + String.join(Constants.SEPARATE_MARKER, data);
        return String.join(Constants.SEPARATE_MARKER, data);
    }

    public static ArrayList<String> unpack(String packed) {
        return new ArrayList<>(Arrays.asList(packed.split(Constants.SEPARATE_MARKER)));
    }

    public static String getReceivedType(String received) {
        return unpack(received).get(0);
    }

    public static String generateImagePath() {
        return Paths.get(
                Paths.get("").toAbsolutePath().toString(),
                Constants.imagesPath,
                RandomString.make(16) + ".png"
        ).toString();
    }
}

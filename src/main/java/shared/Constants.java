package shared;

import java.nio.file.Paths;

public class Constants {
    public static String TEXT_MESSAGE_EVENT = "TEXT_MESSAGE_EVENT";
    public static String FILE_MESSAGE_EVENT = "FILE_MESSAGE_EVENT";
    public static String USER_LOGGED = "ONLINE_USERS_EVENT";
    public static String LIST_CONVERSATIONS_EVENT = "LIST_CONVERSATIONS_EVENT";
    public static String IMAGE_MESSAGE_EVENT = "IMAGE_MESSAGE_EVENT";
    public static String FIND_CONVERSATION_WITH_USERS = "FIND_CONVERSATION_WITH_USERS";
    public static String GET_MESSAGES_IN_CONVERSATION_EVENT = "GET_MESSAGES_IN_CONVERSATION_EVENT";
    public static String NEW_CONVERSATION_EVENT = "NEW_CONVERSATION_EVENT";
    public static String FIND_CONTACT = "FIND_CONTACT";
    public static String VIDEO_CALL_EVENT = "VIDEO_CALL_EVENT";
    public static String REQUEST_DOWNLOAD_FILE = "REQUEST_DOWNLOAD_FILE";

    public static String NOTIFY_USER_ENTERED = "NOTIFY_USER_ENTERED";
    public static String NOTIFY_USER_EXISTED = "NOTIFY_USER_EXISTED";

    public static String TEST = "TEST";

    public static int CHAT_GUI_WIDTH = 800;
    public static int CHAT_GUI_HEIGHT = 600;
    public static int CLIENT_GUI_WIDTH = 300;
    public static int CLIENT_GUI_HEIGHT = 600;
    public static int MAX_SIZE_SOCKET = 65536;

    public static int PORT = 3000;
    public static String URL = "localhost";

    public static String SEPARATE_MARKER = ";";
    public static String imagesPath = Paths.get("storage", "images").toString();
    public static String filesPath = Paths.get("storage", "files").toString();
    public static String conversationBtw2SplitChar = "/";

}

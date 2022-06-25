package cclient;

import model.User;

import java.util.ArrayList;
import java.util.List;

public class ClientGUIController {

    ClientGUI gui;
    public List<ChatGUI> chatManager;

    public ClientGUIController(ClientGUI gui) {
        this.gui = gui;
        this.chatManager = new ArrayList<>();
    }

    public void openChatGUIWithUser(User targetUser) {
        ChatGUI chat = new ChatGUI(targetUser);

        chatManager.add(chat);
        chat.setVisible(true);
    }

    public ChatGUI findChatGUI(int convertsationId) {
        for(ChatGUI chatGUI: chatManager) {
            if(chatGUI.controller.getConversation().getId().equals(convertsationId)) return chatGUI;
        }
        return null;
    }
}

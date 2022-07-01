package client;

import dto.UserDto;
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

    public void openChatGUIWithUser(UserDto targetUser) {
        ChatGUI chat = new ChatGUI();
        ChatGUIController chatController = new ChatGUIController(chat, targetUser);

        chat.controller = chatController;
        chatManager.add(chat);
        chatController.initConversation();;

        chat.initGUI();
    }

    public ChatGUI findChatGUI(int convertsationId) {
        for(ChatGUI chatGUI: chatManager) {
            if(chatGUI.controller.getConversation().getId().equals(convertsationId)) return chatGUI;
        }
        return null;
    }
}

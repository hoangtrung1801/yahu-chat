package client;

import dto.ConversationDto;
import dto.UserDto;

import java.util.ArrayList;
import java.util.List;

public class ClientGUIController {

    ClientGUI gui;
    ChatGUI chatGUI;

    public List<ChatGUI> chatManager;

    public ClientGUIController(ClientGUI gui) {
        this.gui = gui;
        this.chatManager = new ArrayList<>();

        chatGUI = new ChatGUI();
    }

    public void openChatGUIWithConversation(ConversationDto conversation) {
        chatGUI.controller.addConversation(conversation);
        if(!chatGUI.isVisible()) chatGUI.setVisible(true);
    }

    public ChatGUI findChatGUI(int convertsationId) {
        for(ChatGUI chatGUI: chatManager) {
            if(chatGUI.controller.getConversation().getId().equals(convertsationId)) return chatGUI;
        }
        return null;
    }
}

package client;

import dto.ConversationDto;
import dto.UserDto;

import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class ClientGUIController {

    protected ClientGUI gui;
    protected ChatGUI chatGUI;
    protected VideoCallGUI videoCallGUI;

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

    public void callVideoInConversation(ConversationDto conversation) {
        if(videoCallGUI != null && videoCallGUI.window.isDisplayable()) {
            videoCallGUI.setVisible(true);
            return;
        }

        videoCallGUI = new VideoCallGUI("Call video in " + conversation.getConversationName(), conversation);
        videoCallGUI.setVisible(true);

        videoCallGUI.controller.startCall();
    }

    public void updateVideoCall(BufferedImage image) {
        if(videoCallGUI == null) return;
        videoCallGUI.updateImage(image);
    }

    public ChatGUI findChatGUI(int convertsationId) {
        for(ChatGUI chatGUI: chatManager) {
            if(chatGUI.controller.getConversation().getId().equals(convertsationId)) return chatGUI;
        }
        return null;
    }
}

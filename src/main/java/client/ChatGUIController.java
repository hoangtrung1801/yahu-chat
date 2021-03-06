package client;

import client.components.ConversationTab;
import dto.*;
import org.modelmapper.ModelMapper;
import shared.Constants;
import shared.Helper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ChatGUIController {

    private UserDto targetUser;
    private ConversationDto conversation;
    protected List<MessageDto> messagesSentBefore;
    protected List<ConversationTab> conversationTabs;

    public ChatGUI gui;

    public ChatGUIController(ChatGUI gui) {
        this.gui = gui;
        conversationTabs = new ArrayList<>();
    }

    public void initConversationTab(ConversationTab conversationTab, ConversationDto conversation ) {
        //
        ModelMapper modelMapper = new ModelMapper();
        gui.getTabConversation().addTab(Helper.getConversationNameFromConversation(conversation, modelMapper.map(ChatClient.user, UserDto.class)), conversationTab);
        gui.requestFocusInTab(conversationTab);

        ChatClient.connection.sendGetMessagesInConversation(conversation);
    }

    public void addConversation(ConversationDto conversation) {
        for(ConversationTab tab : conversationTabs)
            if(tab.getConversation().equals(conversation)) {
                // if conversation tab have already opened
                gui.requestFocusInTab(tab);
                return;
            }

        ConversationTab conversationTab = new ConversationTab(gui, conversation);
        conversationTabs.add(conversationTab);
        conversationTab.setListener(new ConversationTab.ConversationTabListener() {
            @Override
            public void sendTextMessage(String text) {
                ChatGUIController.this.sendTextMessage(conversation, text);
            }

            @Override
            public void sendImageMessage(File imageFile) {
                ChatGUIController.this.sendImageMessage(conversation, imageFile);
            }

            @Override
            public void sendFileMessage(File file) {
                ChatGUIController.this.sendFileMessage(conversation, file);
            }

            @Override
            public void sendEmojiMessage() {

            }
        });

        initConversationTab(conversationTab, conversation);
    }

//    public void setupMessageInConversationTwoUser(List<UserDto> users, List<MessageDto> messages) {
//        ConversationTab tab = null;
//        for(ConversationTab ctab: conversationTabs)
//            if(users.contains(ctab.getTargetUser()))
//                tab = ctab;
//
//        for(MessageDto message: messages) {
//            switch (message.getMessageType()) {
//                case TEXT -> tab.appendTextMessage(message.getUser().getUsername(), message.getMessageText());
//                case IMAGE -> tab.appendImage(message.getUser().getUsername(), ((ImageMessageDto) message).getImage());
//            }
//        }
//    }

    public void setupMessageInConversation(ConversationDto conversation, List<MessageDto> messages) {
        ConversationTab tab = null;
        for(ConversationTab ctab: conversationTabs)
            if(ctab.getConversation().equals(conversation))
                tab = ctab;

        if(tab == null) {
            System.out.println("tab null");
            return;
        }

        for(MessageDto message: messages) {
            switch (message.getMessageType()) {
                case TEXT -> tab.appendTextMessage(message);
                case IMAGE -> tab.appendImage((ImageMessageDto) message);
                case FILE -> tab.appendFile((FileMessageDto) message);
            }
        }
    }

    // -------------------------- Action --------------------------
    public void sendTextMessage(ConversationDto conversation, String text) {
        ChatClient.connection.sendMessageInConversation(conversation, text);
    }

    public void sendFileMessage(ConversationDto conversation, File file) {
        try {
            if(!file.isFile()) return;
            FileInputStream fin = new FileInputStream(file);
            byte[] buffer = fin.readAllBytes();
            if(buffer.length > Constants.MAX_SIZE_SOCKET) {
                throw new Exception("Max size for file is " + Constants.MAX_SIZE_SOCKET);
            }

            ChatClient.connection.sendFileInConversation(conversation, file.getName(), buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendImageMessage(ConversationDto conversation, File imageFile) {
        try {
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            ChatClient.connection.sendImageInConversation(conversation, bufferedImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTextMessage(ConversationDto conversation, MessageDto message) {
        ConversationTab tab = findConversationTab(conversation);
        if(tab == null) {
            // TODO new message (have not read yet); show notify
            ChatClient.clientGUI.findConversationCell(conversation).markNotRead();
        } else {
            tab.appendTextMessage(message);
        }
    }

    public void showImageMessage(ConversationDto conversation, ImageMessageDto imageMessageDto) {
        findConversationTab(conversation)
                .appendImage(imageMessageDto);
    }

    public void showFileMessage(ConversationDto conversation, FileMessageDto fileMessageDto) {
        findConversationTab(conversation)
                .appendFile(fileMessageDto);
    }

    public ConversationTab findConversationTab(ConversationDto conversation) {
        for(ConversationTab tab : conversationTabs)
            if(tab.getConversation().equals(conversation)) return tab;
        return null;
    }

    // -------------------------------------------------------------
    public UserDto getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(UserDto targetUser) {
        this.targetUser = targetUser;
    }

    public ConversationDto getConversation() {
        return conversation;
    }

    public void setConversation(ConversationDto conversation) {
        this.conversation = conversation;
    }

    public List<MessageDto> getMessagesSentBefore() {
        return messagesSentBefore;
    }

    public void setMessagesSentBefore(List<MessageDto> messagesSentBefore) {
        this.messagesSentBefore = messagesSentBefore;
    }
}

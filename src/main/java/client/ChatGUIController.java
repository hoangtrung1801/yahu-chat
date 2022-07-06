package client;

import client.components.ConversationTab;
import dto.*;
import org.modelmapper.ModelMapper;
import shared.Constants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ChatGUIController {

    private UserDto targetUser;
    private ConversationDto conversation;
    private List<MessageDto> messagesSentBefore;
    private List<ConversationTab> conversationTabs;

    public ChatGUI gui;

    public ChatGUIController(ChatGUI gui) {
        this.gui = gui;
        conversationTabs = new ArrayList<>();
    }

    public void initConversationTab(ConversationTab conversationTab, ConversationDto conversation ) {
        //
        gui.getTabConversation().addTab(conversation.getConversationName(), conversationTab);

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(ChatClient.user, UserDto.class);
//        ChatClient.connection.sendFindConversationWithUsers(userDto, conversationTab.getTargetUser());
        ChatClient.connection.sendGetMessagesInConversation(conversation);
    }

    public void addConversation(ConversationDto conversation) {
        for(ConversationTab tab : conversationTabs)
            if(tab.getConversation().equals(conversation)) return;

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

//    public void addConversation(UserDto targetUser) {
//        for(ConversationTab tab : conversationTabs)
//            if(tab.getTargetUser().equals(targetUser)) return;
//
//        ConversationTab conversationTab = new ConversationTab(gui, targetUser);
//        conversationTabs.add(conversationTab);
//        System.out.println("Set listener...");
//
//
//        initConversationTab(conversationTab, null);
//    }



    public void setupMessageInConversationTwoUser(List<UserDto> users, List<MessageDto> messages) {
        ConversationTab tab = null;
        for(ConversationTab ctab: conversationTabs)
            if(users.contains(ctab.getTargetUser()))
                tab = ctab;

        for(MessageDto message: messages) {
            switch (message.getMessageType()) {
                case TEXT -> tab.appendTextMessage(message.getUser().getUsername(), message.getMessageText());
                case IMAGE -> tab.appendImage(message.getUser().getUsername(), ((ImageMessageDto) message).getImage());
            }
        }
    }

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
                case TEXT -> tab.appendTextMessage(message.getUser().getUsername(), message.getMessageText());
                case IMAGE -> tab.appendImage(message.getUser().getUsername(), ((ImageMessageDto) message).getImage());
                case FILE -> tab.appendFile(message.getUser().getUsername(), ((FileMessageDto) message).getFilename());
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
        findConversationTab(conversation).appendTextMessage(message.getUser().getUsername(), message.getMessageText());
    }

    public void showImageMessage(ConversationDto conversation, ImageMessageDto imageMessageDto) {
        findConversationTab(conversation)
                .appendImage(
                        imageMessageDto.getUser().getUsername(),
                        imageMessageDto.getImage()
                );
    }

    public void showFileMessage(ConversationDto conversation, FileMessageDto fileMessageDto) {
        findConversationTab(conversation)
                .appendFile(
                        fileMessageDto.getUser().getUsername(),
                        fileMessageDto.getFilename()
                );
    }

//    public void showMessageSentBefore() {
//        for(MessageDto message: messagesSentBefore) {
//            switch (message.getMessageType()) {
//                case TEXT -> gui.appendTextMessage(message.getUser().getUsername(), message.getMessageText());
//                case IMAGE -> gui.appendImage(message.getUser().getUsername(), ((ImageMessageDto) message).getImage());
//            }
//        }
//    }
//
//    public void insertIcon(Emoji emoji) {
//        gui.getInput().setText(gui.getInput().getText() + emoji.getUnicode());
//    }

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

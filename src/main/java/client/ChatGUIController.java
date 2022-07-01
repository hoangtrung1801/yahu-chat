package client;

import dto.ConversationDto;
import dto.ImageMessageDto;
import dto.MessageDto;
import dto.UserDto;
import model.Message;
import model.MessageType;
import org.modelmapper.ModelMapper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class ChatGUIController {

    private UserDto targetUser;
    private ConversationDto conversation;
    private List<MessageDto> messagesSentBefore;

    public ChatGUI gui;

    public ChatGUIController(ChatGUI gui, UserDto targetUser) {
        this.gui = gui;
        this.targetUser = targetUser;
    }

    public void initConversation() {
        // init conversation
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(ChatClient.user, UserDto.class);
        ChatClient.connection.sendFindConversationWithUsers(userDto, targetUser);
    }

    // -------------------------- Action --------------------------
    public void sendTextMessage() {
        String textMessage = gui.inputField.getText();
        gui.inputField.setText("");

        ChatClient.connection.sendMessageInConversation(conversation, textMessage);
    }

    public void sendFileMessage() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            File fileChosen;
            if(fileChooser.showOpenDialog(gui.panel) == JFileChooser.APPROVE_OPTION) {
                fileChosen = fileChooser.getSelectedFile();
            }
//            client.clientConnection.sendFileMessage(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendImageFile(File imageFile) {
        try {
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            ChatClient.connection.sendImageInConversation(conversation, bufferedImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTextMessage(MessageDto messageDto) {
        gui.appendTextMessage(messageDto.getUser().getUsername(), messageDto.getMessageText());
    }

    public void showImageMessage(ImageMessageDto imageMessageDto) {
        gui.appendImage(imageMessageDto.getUser().getUsername(), imageMessageDto.getImage());
    }

    public void showMessageSentBefore() {
        for(MessageDto message: messagesSentBefore) {
            switch (message.getMessageType()) {
                case TEXT -> gui.appendTextMessage(message.getUser().getUsername(), message.getMessageText());
                case IMAGE -> gui.appendImage(message.getUser().getUsername(), ((ImageMessageDto) message).getImage());
            }

        }
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
        showMessageSentBefore();
    }
}

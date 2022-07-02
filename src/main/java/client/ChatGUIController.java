package client;

import dto.*;
import model.Message;
import model.MessageType;
import org.modelmapper.ModelMapper;
import utility.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Arrays;
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

    public void sendFileMessage(File file) {
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

    public void showFileMessage(FileMessageDto fileMessageDto) {
        gui.appendFile(fileMessageDto.getUser().getUsername(), fileMessageDto.getFilename());
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

package client;

import dto.*;
import model.Conversation;
import model.Message;
import model.MessageType;
import model.User;
import org.modelmapper.ModelMapper;
import shared.ConnectionBase;
import utility.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ClientConnection extends ConnectionBase implements Runnable {
    public ClientConnection() {
        super(Constants.URL, Constants.PORT);
        sendUserEnteredEvent();
    }

    @Override
    public void run() {
        boolean isRunning = true;

        while(isRunning) {
            try {
                System.out.println("RECEIVING... ");
                String type = ois.readUTF();
                System.out.println("RECEIVED:  " + type);

                if(type.equals(Constants.ONLINE_USERS_EVENT)) {
                    userEnteredEvent();
                } else if(type.equals(Constants.TEXT_MESSAGE_EVENT)) {
                    textMessageEvent();
                } else if(type.equals(Constants.IMAGE_MESSAGE_EVENT)) {
                    imageMessageEvent();
                } else if(type.equals(Constants.FILE_MESSAGE_EVENT)) {
                    fileMessageEvent();
                } else if(type.equals(Constants.FIND_CONVERSATION_WITH_USERS)) {
                    findConversationWithUsers();
                }
            } catch (Exception e) {
                e.printStackTrace();
                isRunning = false;
            }
        }

        close();
    }

    // --------------- EVENT -----------------
    private void userEnteredEvent() {
        try {
            Set<UserDto> onlineUsers = (Set<UserDto>) ois.readObject();
            ChatClient.clientGUI.updateOnlineUsersPanel(onlineUsers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void textMessageEvent() {
        try {
            MessageDto messageDto = (MessageDto) ois.readObject();

            ChatGUI chatGUI = ChatClient.clientGUI.controller.findChatGUI(messageDto.getConversation().getId());
            chatGUI.controller.showTextMessage(messageDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void imageMessageEvent() {
        try {
            ImageMessageDto imageMessageDto = (ImageMessageDto) ois.readObject();

            ChatGUI chatGUI = ChatClient.clientGUI.controller.findChatGUI(imageMessageDto.getConversation().getId());
            chatGUI.controller.showImageMessage(imageMessageDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fileMessageEvent() {
        try {
            FileMessageDto fileMessageDto = (FileMessageDto) ois.readObject();

            ChatGUI chatGUI = ChatClient.clientGUI.controller.findChatGUI(fileMessageDto.getConversation().getId());
            chatGUI.controller.showFileMessage(fileMessageDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findConversationWithUsers() {
        try {
            ConversationDto conversationDto = (ConversationDto) ois.readObject();
            List<MessageDto> messageDtos = (List<MessageDto>) ois.readObject();

            ChatGUI chatGUI = ChatClient
                    .clientGUI
                    .controller
                    .chatManager.get(
                            ChatClient.clientGUI.controller.chatManager.size() - 1
                    );

            chatGUI.controller.setConversation(conversationDto);
            chatGUI.controller.setMessagesSentBefore(messageDtos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --------------- ACTION -----------------
    public void sendUserEnteredEvent() {
        /*
            ONLINE_USERS_EVENT
            userId
         */
        sendData(Constants.ONLINE_USERS_EVENT);
        sendData(ChatClient.user.getId()+"");
    }

    public void sendMessageInConversation(ConversationDto conversation, String textMessage) {
        /*
            TEXT_MESSAGE_EVENT
            MessageDto
         */

        sendData(Constants.TEXT_MESSAGE_EVENT);

        ModelMapper modelMapper = new ModelMapper();
        MessageDto messageDto = new MessageDto(null, textMessage, null, conversation, modelMapper.map(ChatClient.user, UserDto.class));
        sendObject(messageDto);
    }

    public void sendImageInConversation(ConversationDto conversation, BufferedImage bufferedImage) {
        /*
            IMAGE_MESSAGE_EVENT
            ImageMessageDto
         */
        try {
            sendData(Constants.IMAGE_MESSAGE_EVENT);

            ModelMapper modelMapper = new ModelMapper();
            ImageMessageDto imageMessageDto = new ImageMessageDto(
                    MessageType.IMAGE,
                    "",
                    bufferedImage,
                    Instant.now(),
                    conversation,
                    modelMapper.map(ChatClient.user, UserDto.class)
            );
            sendObject(imageMessageDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendFileInConversation(ConversationDto conversation, String filename, byte[] buffer) {
        /*
            FILE_MESSAGE_EVENT
            FileMessageDto
         */
        sendData(Constants.FILE_MESSAGE_EVENT);

        ModelMapper modelMapper = new ModelMapper();

        FileMessageDto fileMessageDto = new FileMessageDto(
                MessageType.FILE,
                "",
                filename,
                buffer,
                Instant.now(),
                conversation,
                modelMapper.map(ChatClient.user, UserDto.class)
        );
        sendObject(fileMessageDto);
    }

    public void sendFindConversationWithUsers(UserDto... users) {
        sendData(Constants.FIND_CONVERSATION_WITH_USERS);
        sendObject(Arrays.asList(users));
    }
}


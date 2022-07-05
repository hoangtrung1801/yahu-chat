package client;

import client.components.ConversationTab;
import dto.*;
import model.MessageType;
import org.modelmapper.ModelMapper;
import shared.ConnectionBase;
import utility.Constants;

import java.awt.image.BufferedImage;
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
                    onlineUsersEvent();
                } else if(type.equals(Constants.LIST_CONVERSATIONS_EVENT)) {
                    listConversationsEvent();
                } else if(type.equals(Constants.TEXT_MESSAGE_EVENT)) {
                    textMessageEvent();
                } else if(type.equals(Constants.IMAGE_MESSAGE_EVENT)) {
                    imageMessageEvent();
                } else if(type.equals(Constants.FILE_MESSAGE_EVENT)) {
                    fileMessageEvent();
                } else if(type.equals(Constants.FIND_CONVERSATION_WITH_USERS)) {
                    findConversationWithUsers();
                } else if(type.equals(Constants.GET_MESSAGES_IN_CONVERSATION_EVENT)) {
                    getMessagesInConversationEvent();
                }
            } catch (Exception e) {
                e.printStackTrace();
                isRunning = false;
            }
        }

        close();
    }

    // --------------- EVENT -----------------
    private void onlineUsersEvent() {
        try {
            Set<UserDto> onlineUsers = (Set<UserDto>) ois.readObject();
            ChatClient.clientGUI.updateOnlineUsersPanel(onlineUsers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listConversationsEvent() {
        try {
            List<ConversationDto> listConversations = (List<ConversationDto>) ois.readObject();
            ChatClient.clientGUI.updateListConversations(listConversations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void textMessageEvent() {
        try {
            MessageDto messageDto = (MessageDto) ois.readObject();

            ChatGUI chatGUI = ChatClient.clientGUI.controller.chatGUI;
            chatGUI.controller.showTextMessage(messageDto.getConversation(), messageDto);
//            ChatGUI chatGUI = ChatClient.clientGUI.controller.findChatGUI(messageDto.getConversation().getId());
//            chatGUI.controller.showTextMessage(messageDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void imageMessageEvent() {
        try {
            ImageMessageDto imageMessageDto = (ImageMessageDto) ois.readObject();

            ChatGUI chatGUI = ChatClient.clientGUI.controller.chatGUI;
            chatGUI.controller.showImageMessage(imageMessageDto.getConversation(), imageMessageDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fileMessageEvent() {
        try {
            FileMessageDto fileMessageDto = (FileMessageDto) ois.readObject();

            ChatGUI chatGUI = ChatClient.clientGUI.controller.chatGUI;
            chatGUI.controller.showFileMessage(fileMessageDto.getConversation(), fileMessageDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findConversationWithUsers() {
        try {
//            ConversationDto conversation = (ConversationDto) ois.readObject();
            List<UserDto> users = (List<UserDto>) ois.readObject();
            List<MessageDto> messages = (List<MessageDto>) ois.readObject();

            ChatGUI chatGUI = ChatClient.clientGUI.controller.chatGUI;
            chatGUI.controller.setupMessageInConversationTwoUser(users, messages);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMessagesInConversationEvent() {
        try {
            ConversationDto conversation = (ConversationDto) ois.readObject();
            List<MessageDto> messages = (List<MessageDto>) ois.readObject();

            ChatGUI chatGUI = ChatClient.clientGUI.controller.chatGUI;
            chatGUI.controller.setupMessageInConversation(conversation, messages);
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
//        sendData(Constants.ONLINE_USERS_EVENT);
        sendData(Constants.LIST_CONVERSATIONS_EVENT);
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

    public void sendGetMessagesInConversation(ConversationDto conversation) {
        sendData(Constants.GET_MESSAGES_IN_CONVERSATION_EVENT);
        sendObject(conversation);
    }

}
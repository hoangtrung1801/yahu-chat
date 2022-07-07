package client;

import dto.*;
import model.MessageType;
import model.User;
import org.imgscalr.Scalr;
import org.modelmapper.ModelMapper;
import shared.ConnectionBase;
import shared.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClientConnection extends ConnectionBase implements Runnable {
    public ClientConnection() {
        super(Constants.URL, Constants.PORT);
        sendUserLogged();
    }

    @Override
    public void run() {
        boolean isRunning = true;

        synchronized (this) {
            while (isRunning) {
                try {
                    System.out.println("RECEIVING... ");
                    String type = ois.readUTF();
                    System.out.println("RECEIVED:  " + type);

                    if (type.equals(Constants.LIST_CONVERSATIONS_EVENT)) {
                        listConversationsEvent();
                    } else if (type.equals(Constants.TEXT_MESSAGE_EVENT)) {
                        textMessageEvent();
                    } else if (type.equals(Constants.IMAGE_MESSAGE_EVENT)) {
                        imageMessageEvent();
                    } else if (type.equals(Constants.FILE_MESSAGE_EVENT)) {
                        fileMessageEvent();
                    } else if (type.equals(Constants.FIND_CONVERSATION_WITH_USERS)) {
                        findConversationWithUsers();
                    } else if (type.equals(Constants.GET_MESSAGES_IN_CONVERSATION_EVENT)) {
                        getMessagesInConversationEvent();
                    } else if (type.equals(Constants.GET_USERS_IN_CONVERSATION_EVENT)) {
                        getUsersInConversationEvent();
                    } else if (type.equals(Constants.FIND_CONTACT)) {
                        findContact();
                    } else if(type.equals(Constants.VIDEO_CALL_EVENT)) {
                        videoCallEvent();
                    } else if(type.equals(Constants.REQUEST_DOWNLOAD_FILE)) {
                        requestDownloadFile();
                    } else if(type.equals(Constants.FIND_USER_NEW_GROUP)) {
                        findUserNewGroup();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    isRunning = false;
                }
            }
        }
        close();
    }

    // --------------- EVENT -----------------
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
//            chatGUI.controller.setupMessageInConversationTwoUser(users, messages);
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

    private void getUsersInConversationEvent() {
        try {
            ConversationDto conversation = (ConversationDto) ois.readObject();
            List<UserDto> users = (List<UserDto>) ois.readObject();

            ChatGUI chatGUI = ChatClient.clientGUI.controller.chatGUI;
            chatGUI.controller.setupUsersInConversation(conversation, users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findContact() {
        try {
            List<UserDto> users = (List<UserDto>) ois.readObject();
            ChatClient.clientGUI.getNewContactGUI().foundContact(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findUserNewGroup() {
        try {
            List<UserDto> userDtos = (List<UserDto>) ois.readObject();
            ChatClient.clientGUI.getNewGroupUsersGUI().foundUsers(userDtos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void videoCallEvent() {
        try {
            VideoCallData videoCallData = (VideoCallData) ois.readObject();
            byte[] image = videoCallData.getImage();
            InputStream is = new ByteArrayInputStream(image);
            BufferedImage buffer = ImageIO.read(is);

            ChatClient.clientGUI.controller.updateVideoCall(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestDownloadFile() {
        try {
            FileRespone fileRespone = (FileRespone) ois.readObject();
            byte[] fileBuffer = fileRespone.getFileBuffer();

            FileOutputStream fos = new FileOutputStream(new File(fileRespone.getFilenameOut()));
            fos.write(fileBuffer, 0, fileBuffer.length);
            JOptionPane.showMessageDialog(null, "Download successfull");

            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --------------- ACTION -----------------
    public void sendUserLogged() {
        /*
            LIST_CONVERSATION_EVENT
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

//    public void sendGetsUsersInConversation(ConversationDto conversation) {
//        sendData(Constants.GET_USERS_IN_CONVERSATION_EVENT);
//        sendObject(conversation);
//    }

    public void sendFindContact(String target) {
        sendData(Constants.FIND_CONTACT);
        sendData(target);
    }

    public void sendFindUserNewGroup(String target) {
        sendData(Constants.FIND_USER_NEW_GROUP);
        sendData(target);
    }

    public void sendNewConversationWithUser(UserDto targetUser) {
        ModelMapper modelMapper = new ModelMapper();
        List<UserDto> users = new ArrayList<>(Arrays.asList(modelMapper.map(ChatClient.user, UserDto.class), targetUser));

        sendData(Constants.NEW_CONVERSATION_EVENT);
        sendObject(users);
        sendData(users.stream().map(UserDto::getUsername).collect(Collectors.joining(Constants.conversationBtw2SplitChar)));
    }

    public void sendNewConversationWithMutilUser(List<UserDto> users, String conversationName) {
        sendData(Constants.NEW_CONVERSATION_EVENT);
        sendObject(users);
        sendData(conversationName);
    }

    public void sendVideoCallData(ConversationDto conversation, BufferedImage buffer) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(buffer, "jpg", baos);
            byte[] image = baos.toByteArray();
            VideoCallData videoCallData = new VideoCallData(conversation, image);

            sendData(Constants.VIDEO_CALL_EVENT);
            sendObject(videoCallData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendRequestDownloadFile(String filename, String filenameOut) {
        ModelMapper modelMapper = new ModelMapper();
        FileRequest fileRequest = new FileRequest(modelMapper.map(ChatClient.user, UserDto.class), filename, filenameOut);

        sendData(Constants.REQUEST_DOWNLOAD_FILE);
        sendObject(fileRequest);
    }
}
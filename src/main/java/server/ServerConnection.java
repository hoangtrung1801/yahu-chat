package server;

import dao.ConversationDAO;
import dao.MessageDAO;
import dao.UserDAO;
import dao.implement.ConversationDAOImpl;
import dao.implement.MessageDAOImpl;
import dao.implement.UserDAOImpl;
import dto.*;
import model.*;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import shared.ConnectionBase;
import utility.Constants;
import utility.HibernateUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ServerConnection extends ConnectionBase implements Runnable {

    public User user;
    private final UserDAO userDAO;
    private final ConversationDAO conversationDAO;
    private final MessageDAO messageDAO;

    public ServerConnection(Socket socket) {
        super(socket);

        userDAO = new UserDAOImpl();
        conversationDAO = new ConversationDAOImpl();
        messageDAO = new MessageDAOImpl();
    }

    @Override
    public synchronized void run() {
        boolean isRunning = true;
        while(isRunning) {
            try {
                System.out.println("...");
                String type = ois.readUTF();
                System.out.println("RECEIVED: " + type);
                ChatServer.gui.log(user, "RECEIVED: " + type);

                if(type.equals(Constants.USER_LOGGED)) {
                    userLogged();
                } else if(type.equals(Constants.LIST_CONVERSATIONS_EVENT)) {
                    listConversationsEvent();
                } else if(type.equals(Constants.TEXT_MESSAGE_EVENT)) {
                    textMesageEvent();
                } else if(type.equals(Constants.IMAGE_MESSAGE_EVENT)) {
                    imageMessageEvent();
                } else if(type.equals(Constants.FILE_MESSAGE_EVENT)) {
                    fileMessageEvent();
                } else if(type.equals(Constants.FIND_CONVERSATION_WITH_USERS)) {
                    findConversationWithUsers();
                } else if(type.equals(Constants.GET_MESSAGES_IN_CONVERSATION_EVENT)) {
                    getMessagesInConversationEvent();
                } else if(type.equals(Constants.NEW_CONVERSATION_EVENT)) {
                    newConversationEvent();
                } else if (type.equals(Constants.FIND_CONTACT)) {
                    findContact();
                }
            } catch (Exception e) {
                e.printStackTrace();
                isRunning = false;
                System.out.println("CLIENT EXISTED");

                ChatServer.gui.log(user, "ERROR: " + e.getMessage());
                ChatServer.gui.log(user, "CLIENT EXISTED");
            }
        }

        ChatServer.connectionManager.remove(this);
        close();
    }

    // --------------- EVENT -----------------
    private void userLogged() {
        try {
            int userId = Integer.parseInt(ois.readUTF());
            user = userDAO.readById(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listConversationsEvent() {
        try {
            ModelMapper modelMapper = new ModelMapper();

            // get user information
            int userId = Integer.parseInt(ois.readUTF());
            user = userDAO.readById(userId);

            // refresh
            HibernateUtils.refresh(user);

            Set<GroupMember> groupMembers = user.getGroupMembers();
            List<Conversation> conversations= groupMembers.stream().map(GroupMember::getConversation).toList();
            List<ConversationDto> conversationDtos = conversations.stream().map(conversation -> modelMapper.map(conversation, ConversationDto.class)).toList();


            sendData(Constants.LIST_CONVERSATIONS_EVENT);
            sendObject(conversationDtos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void textMesageEvent() {
        try {
            ModelMapper modelMapper = new ModelMapper();

            MessageDto messageDto = (MessageDto) ois.readObject();
            ConversationDto conversationDto = messageDto.getConversation();
            UserDto senderUserDto = messageDto.getUser();
            String textMessage = messageDto.getMessageText();

            Conversation conversation = conversationDAO.readById(conversationDto.getId());
            User user = userDAO.readById(senderUserDto.getId());

            Message message = new Message(textMessage, Instant.now());
            message.setConversation(conversation);
            message.setUser(user);
            message = messageDAO.create(message);

            messageDto = modelMapper.map(message, MessageDto.class);

            for(GroupMember gm: conversation.getGroupMembers()) {
                User receiver = gm.getUser();
                ServerConnection sc = ChatServer.connectionManager.findWithUser(receiver);
                if(sc != null) {
                    sc.sendData(Constants.TEXT_MESSAGE_EVENT);
                    sc.sendObject(messageDto);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void imageMessageEvent() {
        try {
            ModelMapper modelMapper = new ModelMapper();
            ImageMessageDto imageMessageDto = (ImageMessageDto) ois.readObject();
            BufferedImage bufferedImage = imageMessageDto.getImage();

            // save image to storage
            String filepath = Paths.get(Constants.imagesPath, RandomString.make(16) + ".png").toString();
            String absoluteFilepath = Paths.get(
                    Paths.get("").toAbsolutePath().toString(),
                    filepath
            ).toString();
            File file = new File(absoluteFilepath);
            ImageIO.write(bufferedImage, "png", file);

            // create new message
            Message message = modelMapper.map(imageMessageDto, Message.class);
            message.setAttachmentUrl(filepath);
            message = messageDAO.create(message);

            Conversation conversation = conversationDAO.readById(message.getConversation().getId());

            imageMessageDto.setId(message.getId());
            for(GroupMember gm: conversation.getGroupMembers()) {
                User receiver = gm.getUser();
                ServerConnection sc = ChatServer.connectionManager.findWithUser(receiver);
                sc.sendData(Constants.IMAGE_MESSAGE_EVENT);
                sc.sendObject(imageMessageDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fileMessageEvent() {
        try {
            ModelMapper modelMapper = new ModelMapper();
            FileMessageDto fileMessageDto = (FileMessageDto) ois.readObject();
            byte[] buffer = fileMessageDto.getBuffer();

            // generate filepath
            String filepath = Paths.get(Constants.filesPath, fileMessageDto.getFilename()).toString();
            String absoluteFilepath = Paths.get(
                    Paths.get("").toAbsolutePath().toString(),
                    filepath
            ).toString();

            // save file into storage
            File file = new File(absoluteFilepath);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(buffer, 0, buffer.length);

            // create new message
            Message message = modelMapper.map(fileMessageDto, Message.class);
            message.setAttachmentUrl(filepath);
            message = messageDAO.create(message);

            fileMessageDto.setId(message.getId());

            // send message to users
            Conversation conversation = conversationDAO.readById(message.getConversation().getId());
            for(GroupMember gm: conversation.getGroupMembers()) {
                User receiver = gm.getUser();
                ServerConnection sc = ChatServer.connectionManager.findWithUser(receiver);
                sc.sendData(Constants.FILE_MESSAGE_EVENT);
                sc.sendObject(fileMessageDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void findConversationWithUsers() {
        /*
            FIND_CONVERSATION_WITH_USERS
            // ConversationDto
            List<UserDto>
            List<MessageDto>
         */
        try {
            ModelMapper modelMapper = new ModelMapper();
            List<UserDto> userDtos = (List<UserDto>) ois.readObject();
            List<User> users = userDtos.stream().map(
                    user -> modelMapper.map(user, User.class)
            ).toList();

            Conversation conversation = conversationDAO.findConversationWithUsers(
                    users.toArray(new User[0])
            );

            // if null then create new
            if(conversation == null) {
                Conversation newConversation = new Conversation();
                newConversation.setConversationName(
                        users.stream().map(User::getUsername).collect(Collectors.joining("  "))
                );
                conversation = conversationDAO.create(newConversation);

                Set<GroupMember> groupMembers = new HashSet<>();
                for(User user: users) {
                    GroupMember gmUser = new GroupMember(user, conversation, Instant.now(), null);
                    groupMembers.add(gmUser);
                }

                conversation.setGroupMembers(groupMembers);
                conversation = conversationDAO.update(conversation);

            }

            // refresh
            HibernateUtils.refresh(conversation);

            // send back conversation
            sendData(Constants.FIND_CONVERSATION_WITH_USERS);
//            sendObject(conversationDto);
            sendObject(userDtos);

            List<MessageDto> messages = conversation.getMessages().stream().map(message -> {
                if(message.getMessageType().equals(MessageType.TEXT)) {
                    return modelMapper.map(message, MessageDto.class);
                } else if(message.getMessageType().equals(MessageType.IMAGE)) {
                    try {
//                        MessageDto messageDto = modelMapper.map(message, MessageDto.class);
                        ImageMessageDto imageMessageDto = modelMapper.map(message, ImageMessageDto.class);
                        imageMessageDto.setImage(ImageIO.read(new File(message.getAttachmentUrl())));

                        return imageMessageDto;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }).toList();
            sendObject(messages);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getMessagesInConversationEvent() {
         /*
         in:
            GET_MESSAGE_IN_CONVERSATOIN
            ConversationDto

         out:
         if not null:
            GET_MESSAGE_IN_CONVERSATOIN
            ConversationDto
            List<MessageDto>

         if null :
            GET_MESSAGE_IN_CONVERSATOIN
            ConversationDto
            null
         */
        try {
            ModelMapper modelMapper = new ModelMapper();

            ConversationDto conversationDto = (ConversationDto) ois.readObject();
            Conversation conversation = conversationDAO.readById(conversationDto.getId());

            // send command
            sendData(Constants.GET_MESSAGES_IN_CONVERSATION_EVENT);
            sendObject(conversationDto);

            // if null then create new
            if(conversation == null) {
                sendObject(null);
            }

            // refresh
            HibernateUtils.refresh(conversation);

            // send back conversation
            List<MessageDto> messages = conversation.getMessages().stream().map(message -> {
                if(message.getMessageType().equals(MessageType.TEXT)) {
                    return modelMapper.map(message, MessageDto.class);
                } else if(message.getMessageType().equals(MessageType.IMAGE)) {
                    try {
                        ImageMessageDto imageMessageDto = modelMapper.map(message, ImageMessageDto.class);
                        imageMessageDto.setImage(ImageIO.read(new File(message.getAttachmentUrl())));
                        return imageMessageDto;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if(message.getMessageType().equals(MessageType.FILE)) {
                    try {
                        FileMessageDto fileMessageDto = modelMapper.map(message, FileMessageDto.class);
                        List<String> fileNameSplit = Arrays.stream(message.getAttachmentUrl().split("/")).toList();
                        String filename = fileNameSplit.get(fileNameSplit.size()-1);
                        fileMessageDto.setFilename(filename);
                        return fileMessageDto;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }).toList();
            sendObject(messages);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void newConversationEvent() {
        /*
        in:
            NEW_CONVERSATION_EVENT
            List<UserDto>

        out:
            NEW_CONVERSATION_EVENT
            Conversation
         */
        try {
            ModelMapper modelMapper = new ModelMapper();
            List<UserDto> userDtos = (List<UserDto>) ois.readObject();
            List<User> users = userDtos.stream().map(user -> userDAO.readById(user.getId())).toList();

            Conversation conversation = new Conversation();
            conversation.setConversationName(
                    userDtos.stream().map(UserDto::getUsername).collect(Collectors.joining(", "))
            );
            conversation = conversationDAO.create(conversation);

//            Conversation finalConversation = conversation;
//            users.forEach(user -> {
//                GroupMember gm = new GroupMember(user, finalConversation, Instant.now(), null);
//            });
            Conversation finalConversation = conversation;
            conversation.setGroupMembers(
                users.stream().map(user -> new GroupMember(user, finalConversation, Instant.now(), null)).collect(Collectors.toSet())
            );
            conversation = conversationDAO.update(conversation);
            if(conversation == null) {
                throw new Error("Conversation is null");
            }

            // send
//            sendData(Constants.NEW_CONVERSATION_EVENT);
//            sendObject(conversationDto);
            for(GroupMember gm: conversation.getGroupMembers()) {
                sendListConversationEvent(gm.getUser());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void findContact() {
        try {
            ModelMapper modelMapper = new ModelMapper();
            String target = ois.readUTF();
            List<User> users = userDAO.findContact(target);
            List<UserDto> userDtos = users.stream().map(user -> modelMapper.map(user, UserDto.class)).toList();

            // send
            sendData(Constants.FIND_CONTACT);
            sendObject(userDtos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --------------- ACTION -----------------
    public void sendListConversationEvent(User user) {
        ModelMapper modelMapper = new ModelMapper();
        HibernateUtils.refresh(user);
        Set<GroupMember> groupMembers = user.getGroupMembers();
        List<Conversation> conversations= groupMembers.stream().map(GroupMember::getConversation).toList();
        List<ConversationDto> conversationDtos = conversations.stream().map(conversation -> modelMapper.map(conversation, ConversationDto.class)).toList();

        sendData(Constants.LIST_CONVERSATIONS_EVENT);
        sendObject(conversationDtos);
    }
}

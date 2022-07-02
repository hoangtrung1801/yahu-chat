package server;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
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
import utility.Helper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
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
                System.out.println("RECEIVING... ");
                String type = ois.readUTF();
                System.out.println("RECEIVED: " + type);

                if(type.equals(Constants.ONLINE_USERS_EVENT)) {
                    userEnteredEvent();
                } else if(type.equals(Constants.TEXT_MESSAGE_EVENT)) {
                    textMesageEvent();
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
                System.out.println("CLIENT EXISTED");
            }
        }

        ChatServer.connectionManager.remove(this);
        close();
    }

    // --------------- EVENT -----------------
    private void userEnteredEvent() {
        try {
            int userId = Integer.parseInt(ois.readUTF());
            System.out.println(userId);
            user = userDAO.readById(userId);
            ChatServer.sendOnlineUsersEvent();
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
                sc.sendData(Constants.TEXT_MESSAGE_EVENT);
                sc.sendObject(messageDto);
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

            // convert conversation to dto
            ConversationDto conversationDto = modelMapper.map(conversation, ConversationDto.class);

            // send back conversation
            sendData(Constants.FIND_CONVERSATION_WITH_USERS);
            sendObject(conversationDto);

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

    // --------------- ACTION -----------------
}

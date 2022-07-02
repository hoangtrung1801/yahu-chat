import dao.ConversationDAO;
import dao.MessageDAO;
import dao.UserDAO;
import dao.implement.ConversationDAOImpl;
import dao.implement.MessageDAOImpl;
import dao.implement.UserDAOImpl;
import dto.UserDto;
import model.Message;
import model.MessageType;
import model.User;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;

import javax.swing.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.logging.Handler;

public class Test {
    public Test() throws Exception {
        run();
    }

    public void run() throws Exception {
//        ModelMapper modelMapper = new ModelMapper();
//
//        UserDAO userDAO = new UserDAOImpl();
//        User user = userDAO.readById(2);
//
//        UserDto userDto = modelMapper.map(user, UserDto.class);
//        userDto.setUsername("hoangtrung");
//        System.out.println(userDto);
//
//        User skeUser = userDAO.readById(2);
//        modelMapper.map(userDto, skeUser);
//        System.out.println(skeUser);

//        MessageDAO messageDAO = new MessageDAOImpl();
//        UserDAO userDAO = new UserDAOImpl();
//        ConversationDAO conversationDAO = new ConversationDAOImpl();
//        Message message = new Message(MessageType.TEXT, "hello", Instant.now());
//        message.setUser(userDAO.readById(2));
//        message.setConversation(conversationDAO.readById(1));
//        message = messageDAO.create(message);
//        System.out.println(message);

//        JFileChooser fileChooser = new JFileChooser();
//        if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
//            File file = fileChooser.getSelectedFile();
//            System.out.println(file.getAbsolutePath());
//        }

//        String path = Paths.get("").toAbsolutePath().toString() + "\\storage\\images\\";
//        String path = Paths.get(
//                Paths.get("").toAbsolutePath().toString(),
//                "storage",
//                "images",
//                RandomString.make(16) + ".png"
//        ).toString();
//        System.out.println(path);
//        System.out.println(Paths.get("storage", "images").toString());

//        File file = new File("D:\\Development\\Java\\ClassOOP\\ChatRealtime\\storage\\images\\39yaHqOGPhfzO1CX.png");
//        System.out.println(file.getName());
    }

    public static void main(String[] args) throws Exception {
        new Test();
    }
}

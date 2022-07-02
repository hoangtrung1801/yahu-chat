import client.emojipicker.EmojiPicker;
import client.emojipicker.EmojiTable;
import client.emojipicker.OpenMojiFont;

import javax.swing.*;
import java.awt.*;

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

//        FlatLightLaf.setup();
//        JFrame frame = new JFrame();
//        JTextPane textPane = new JTextPane();
//        textPane.setPreferredSize(new Dimension(500, 500));
//
//        StyledDocument doc = (StyledDocument) textPane.getDocument();
//
//        String withlove  = "With :heart: Nicoll lê kim hoàng trung :smile:";
//        String result = EmojiParser.parseToUnicode(withlove);
//
////        ImageIcon icon = new ImageIcon(getClass().getResource("assets/send-icon.png"));
////        ImageIcon icon = new ImageIcon(
////                Scalr.resize(ImageIO.read(getClass().getResource("assets/send-icon.png")), 16)
////        );
////        textPane.insertComponent(new JLabel("hello"));
////        textPane.insertIcon(icon);
//
//        textPane.insertComponent(new JLabel(result));
//
//        frame.add(textPane);
//
//        frame.setSize(500, 500);
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.setVisible(true);

        EmojiPicker emojiPicker = new EmojiPicker();
        emojiPicker.setClickListener(emoji -> System.out.println(emoji));
    }

    public static void main(String[] args) throws Exception {
        new Test();
    }
}

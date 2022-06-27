package cclient;

import com.formdev.flatlaf.FlatLightLaf;
import dao.UserDAO;
import dao.implement.UserDAOImpl;
import model.User;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import org.imgscalr.Scalr;
import utilities.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ChatGUI extends JFrame {
    public static void main(String[] args) throws BadLocationException, IOException {
        FlatLightLaf.setup();
        UserDAO userDAO = new UserDAOImpl();
        ChatClient.user = userDAO.readById(5);

        User targetUser = userDAO.readById(8);

        ChatGUI chatGUI = new ChatGUI(targetUser);
        chatGUI.setVisible(true);

//        File imgFile = new File("C:\\Users\\Dell\\Pictures\\269003412_341278631172520_1652515031521655571_n.jpg");
//        BufferedImage bufferedImage = ImageIO.read(imgFile);
    }

    JPanel panel, sendPanel, actionPanel, targetUserPanel;
    JTextPane messageArea;
    JScrollPane messageAreaScroll;
    JTextField inputField;
    JButton sendMessageBtn, sendFileBtn, sendImageBtn;

    StyledDocument messageDocument;

    public ChatGUIController controller;

    public ChatGUI(User targetUser) {
        controller = new ChatGUIController(this, targetUser);

        initGUI();
        controller.showMessagesSentBefore();
    }

    private void initGUI() {
         MigLayout layout = new MigLayout(
                "wrap, fill, debug",
                "[]",
                "[][][]"
        );
        panel = new JPanel(layout);

        initTargetUserPanel();
        initMessageArea();
        initActionPanel();
        initInputPanel();

        setContentPane(panel);

        pack();
        setLocationRelativeTo(null);
//        setVisible(true);

        setTitle(ChatClient.user.getUsername() + " chat with " + controller.getTargetUser().getUsername());
    }

    private void initInputPanel() {
        //
        sendPanel = new JPanel(new MigLayout("ins 0"));

        // Input field
        inputField = new JTextField();
        inputField.setPreferredSize(new Dimension((int) (Constants.CHAT_GUI_WIDTH * 0.95), (int) (Constants.CHAT_GUI_HEIGHT * 0.1)));
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    controller.sendTextMessage();
                }
            }
        });

        sendPanel.add(inputField, new CC().growX().height("100%"));

        // send button
//        ImageIcon sendIcon = new ImageIcon("/assets/send-icon.png");
        sendMessageBtn = new JButton("SEND");
        sendMessageBtn.addActionListener(e -> controller.sendTextMessage());

        sendPanel.add(sendMessageBtn, new CC().height("100%"));
        panel.add(sendPanel, new CC().width(String.valueOf(Constants.CHAT_GUI_WIDTH)).height("32px"));
    }

    private void initActionPanel() {
        // action panel
        actionPanel = new JPanel(new MigLayout("ins 0"));

        sendFileBtn = new JButton("File");
        sendFileBtn.addActionListener(e -> controller.sendFileMessage());
        actionPanel.add(sendFileBtn);

        sendImageBtn = new JButton("Image");
        // show file chooser
        sendImageBtn.addActionListener(e -> {
            JFileChooser fileChooser = new FileChooserImage();
            if(fileChooser.showOpenDialog(panel) == JFileChooser.APPROVE_OPTION) {
                File fileChosen = fileChooser.getSelectedFile();
                controller.sendImageFile(fileChosen);
            }
        });
        actionPanel.add(sendImageBtn);

        panel.add(actionPanel);
    }

    private void initMessageArea() {
        // message area
        messageArea = new JTextPane();
        messageDocument = (StyledDocument) messageArea.getDocument();
        messageAreaScroll = new JScrollPane(messageArea);

        messageArea.setEditable(false);
        messageArea.setBackground(Color.white);

        panel.add(messageAreaScroll, new CC().width(String.valueOf(Constants.CHAT_GUI_WIDTH)).height(String.valueOf(Constants.CHAT_GUI_HEIGHT * 0.8)));
    }

    private void initTargetUserPanel() {
        MigLayout layout = new MigLayout();
        targetUserPanel = new JPanel(layout);

        ImageIcon userIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/user-icon.png")));
        JLabel lUserIcon = new JLabel();
        lUserIcon.setIcon(new ImageIcon(userIcon.getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        targetUserPanel.add(lUserIcon);

        JLabel lUsername = new JLabel(controller.getTargetUser().getUsername());
        targetUserPanel.add(lUsername);

        panel.add(targetUserPanel, new CC().width(String.valueOf(Constants.CHAT_GUI_WIDTH)).height(String.valueOf(Constants.CHAT_GUI_HEIGHT * 0.1)));
    }

    // -------------------------- Chat area --------------------------
    public void appendTextMessage(String name, String textMessage) {
        try {
            messageDocument.insertString(messageDocument.getLength(), name + ": " + textMessage, null);
            insertEndlineDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void appendImage(String name, BufferedImage bufferedImage) {
        try {
            appendTextMessage(name, "sent a image");

            Style style = messageDocument.addStyle("image", null);

            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setAlignment(attr, StyleConstants.ALIGN_CENTER);

            ImageIcon imageIcon = new ImageIcon(Scalr.resize(bufferedImage, 300));

            StyleConstants.setIcon(style, imageIcon);
            messageDocument.insertString(messageDocument.getLength(), "\t", style);
            insertEndlineDocument();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ------------------------------------------------------------
    private void insertEndlineDocument() throws BadLocationException {
        messageDocument.insertString(messageDocument.getLength(), "\n", null);
    }

    // ------------------------------------------------------------
    class FileChooserImage extends JFileChooser {
        JLabel img;
        String[] extensions = {"jpg", "png", "gif", "jpeg"};

        public FileChooserImage() {
            super();

            img = new JLabel();
            img.setPreferredSize(new Dimension(180, 180));
            this.setAccessory(img);

            FileNameExtensionFilter filter = new FileNameExtensionFilter("image extension", extensions);
            this.setFileFilter(filter);

            this.addPropertyChangeListener(evt -> {
                SwingWorker<Image, Void> worker = new SwingWorker<>() {
                    @Override
                    protected Image doInBackground() throws Exception {
                        if (evt.getPropertyName().equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
                            File f = getSelectedFile();
                            BufferedImage bim = ImageIO.read(f);

                            return Scalr.resize(bim, 180);
                        }
                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            Image i = get(1L, TimeUnit.NANOSECONDS);
                            if (i == null) return;
                            img.setIcon(new ImageIcon(i));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                };
                worker.execute();
            });
        }
    }
}

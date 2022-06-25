package cclient;

import model.User;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import utilities.Constants;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatGUI extends JFrame {

    JPanel panel, sendPanel, actionPanel, targetUserPanel;
    JTextPane messageArea;
    JScrollPane messageAreaScroll;
    JTextField inputField;
    JButton sendMessageBtn, sendFileBtn;

    Document messageDocument;

    public ChatGUIController controller;

    public ChatGUI(User targetUser) {
        controller = new ChatGUIController(this, targetUser);
        initGUI();
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
        ImageIcon sendIcon = new ImageIcon("/assets/send-icon.png");
        sendMessageBtn = new JButton("SEND");
        sendMessageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.sendTextMessage();
            }
        });

        sendPanel.add(sendMessageBtn, new CC().height("100%"));
        panel.add(sendPanel, new CC().width(String.valueOf(Constants.CHAT_GUI_WIDTH)).height("32px"));
    }
    private void initActionPanel() {
        // action panel
        actionPanel = new JPanel(new MigLayout("ins 0"));

        sendFileBtn = new JButton("File");
        sendFileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)  {
                controller.sendFileMessage();
            }
        });
        actionPanel.add(sendFileBtn);

        panel.add(actionPanel);
    }

    private void initMessageArea() {
        // message area
        messageArea = new JTextPane();
        messageDocument = messageArea.getDocument();
        messageAreaScroll = new JScrollPane(messageArea);

        messageArea.setEditable(false);
        messageArea.setBackground(Color.white);

        panel.add(messageAreaScroll, new CC().width(String.valueOf(Constants.CHAT_GUI_WIDTH)).height(String.valueOf(Constants.CHAT_GUI_HEIGHT * 0.8)));
    }

    private void initTargetUserPanel() {
        MigLayout layout = new MigLayout();
        targetUserPanel = new JPanel(layout);

        ImageIcon userIcon = new ImageIcon(getClass().getResource("/assets/user-icon.png"));
        JLabel lUserIcon = new JLabel();
        lUserIcon.setIcon(new ImageIcon(userIcon.getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        targetUserPanel.add(lUserIcon);

        JLabel lUsername = new JLabel(controller.getTargetUser().getUsername());
        targetUserPanel.add(lUsername);

        panel.add(targetUserPanel, new CC().width(String.valueOf(Constants.CHAT_GUI_WIDTH)).height(String.valueOf(Constants.CHAT_GUI_HEIGHT * 0.1)));
    }

    // -------------------------- Chat area --------------------------
    public void appendTextUserEntered(String name) {
        try {
            messageDocument.insertString(messageDocument.getLength(), name.toUpperCase() + " entered", null);
            insertEndlineDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void appendTextMessage(String name, String textMessage) {
        try {
            messageDocument.insertString(messageDocument.getLength(), name + ": " + textMessage, null);
            insertEndlineDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ------------------------------------------------------------
    private void insertEndlineDocument() throws BadLocationException {
        messageDocument.insertString(messageDocument.getLength(), "\n", null);
    }
}

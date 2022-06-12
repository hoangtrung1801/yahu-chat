package main.client;

import main.utilities.Constants;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ClientGUI extends JFrame {

    ChatClient client;

    JPanel panel, sendPanel, actionPanel;
    JTextPane messageArea;
    JScrollPane messageAreaScroll;
    JTextField inputField;
    JButton sendMessageBtn, sendFileBtn;

    Document messageDocument;

    public ClientGUI(ChatClient client) {
        this.client = client;

        MigLayout layout = new MigLayout(
                "wrap, fill, debug",
                "[]",
                "[][][]"
        );
        panel = new JPanel(layout);

        // message area
        messageArea = new JTextPane();
        messageDocument = messageArea.getDocument();
        messageAreaScroll = new JScrollPane(messageArea);

        messageArea.setEditable(false);
        messageArea.setBackground(Color.white);

        panel.add(messageAreaScroll, new CC().width(String.valueOf(Constants.GUI_WIDTH)).height(String.valueOf(Constants.GUI_HEIGHT * 0.9)));


        // action panel
        actionPanel = new JPanel(new MigLayout("ins 0"));

        sendFileBtn = new JButton("File");
//        sendFileBtn.addActionListener(new sendFileAction());
        actionPanel.add(sendFileBtn);

        panel.add(actionPanel);

        //
        sendPanel = new JPanel(new MigLayout("ins 0"));

        // Input field
        inputField = new JTextField();
        inputField.setPreferredSize(new Dimension((int) (Constants.GUI_WIDTH * 0.95), (int) (Constants.GUI_HEIGHT * 0.1)));
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendTextMessage();
                }
            }
        });

        sendPanel.add(inputField, new CC().growX().height("100%"));

        // send button
        ImageIcon sendIcon = new ImageIcon("src/main/assets/send-icon.png");
        sendMessageBtn = new JButton("SEND");
        sendMessageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendTextMessage();
            }
        });

        sendPanel.add(sendMessageBtn, new CC().height("100%"));
        panel.add(sendPanel, new CC().width("100%").height("32px"));

        setContentPane(panel);

        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setSize(Constants.GUI_WIDTH, Constants.GUI_HEIGHT);
        pack();
        setLocationRelativeTo(null);
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

    public void appendTextMessage(String senderName, String textMessage) {
        try {
            messageDocument.insertString(messageDocument.getLength(), senderName.toUpperCase() + ": " + textMessage, null);
            insertEndlineDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------- Action --------------------------
    void sendTextMessage() {
        String textMessage = inputField.getText();
        inputField.setText("");

        client.clientConnection.sendTextMessage(textMessage);
    }

    private void insertEndlineDocument() throws BadLocationException {
        messageDocument.insertString(messageDocument.getLength(), "\n", null);
    }
}

/*
 * Created by JFormDesigner on Tue Jul 05 10:54:47 ICT 2022
 */

package client.components;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.swing.*;
import javax.swing.text.*;

import client.ChatGUI;
import client.emojipicker.EmojiPicker;
import dto.ConversationDto;
import dto.GroupMemberDto;
import dto.UserDto;
import net.miginfocom.swing.*;
import org.imgscalr.Scalr;

/**
 * @author unknown
 */
public class ConversationTab extends JPanel {
    public static void main(String[] args) {
        JFrame frame = new JFrame();

        ConversationTab conversationTab = new ConversationTab();
        conversationTab.appendTextMessage("hoangtrung", "hello");

        frame.setContentPane(conversationTab);

        frame.pack();
        frame.setVisible(true);
    }

    public interface ConversationTabListener {
        void sendTextMessage(String text);
        void sendImageMessage(File imageFile);
        void sendFileMessage(File file);
        void sendEmojiMessage();
    }

    private UserDto targetUser;
    private ChatGUI chatGUI;
    private ConversationDto conversation;
    private List<GroupMemberDto> groupMemberList;
    private StyledDocument messageDocument;
    private ConversationTabListener listener;

    public ConversationTab(ChatGUI chatGUI, ConversationDto conversation) {
        this.chatGUI = chatGUI;
        this.conversation = conversation;

        initComponents();
    }

    public ConversationTab(ChatGUI chatGUI, UserDto targetUser) {
        this.chatGUI = chatGUI;
        this.targetUser = targetUser;

        initComponents();
    }

    public ConversationTab() {
        initComponents();
    }

    // ---------------- message area ---------------------------

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

    public void appendFile(String name, String filename) {
        try {
            messageDocument.insertString(messageDocument.getLength(), name + ": sent a file" + filename, null);
            insertEndlineDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertEndlineDocument() throws BadLocationException {
        messageDocument.insertString(messageDocument.getLength(), "\n", null);
    }

    // ------------------ ACTION -----------------------

    private void fileAction(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();

        if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            listener.sendFileMessage(file);
        }
    }

    private void imageAction(ActionEvent e) {
        JFileChooser fileChooser = new FileChooserImage();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File fileChosen = fileChooser.getSelectedFile();
            listener.sendImageMessage(fileChosen);
        }
    }

    private void textMessageAction(ActionEvent e) {
        if(listener == null) {
            System.out.println("Listener is null ?? ");
        }
        String text = input.getText();
        input.setText("");
        listener.sendTextMessage(text);
    }

    private void iconAction(ActionEvent e) {
        EmojiPicker emojiPicker = new EmojiPicker();
        emojiPicker.setClickListener(emoji -> {
            input.setText(input.getText() + emoji.getUnicode());
        });
    }


    // ---------------------------------------------------
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel1 = new JPanel();
        conversation_name = new JLabel();
        panel4 = new JPanel();
        panel5 = new JPanel();
        scrollPane1 = new JScrollPane();
        messagePane = new JTextPane();
        panel6 = new JPanel();
        fileBtn = new JButton();
        imageBtn = new JButton();
        iconBtn = new JButton();
        input = new JTextField();
        sendBtn = new JButton();
        scrollPane2 = new JScrollPane();
        textPane2 = new JTextPane();

        //======== this ========
        setLayout(new MigLayout(
            "insets 0,hidemode 3",
            // columns
            "[grow,fill]",
            // rows
            "[100]" +
            "[24]" +
            "[grow]"));

        //======== panel1 ========
        {
            panel1.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                "[fill]",
                // rows
                "[]" +
                "[]" +
                "[]"));
        }
        add(panel1, "cell 0 0");

        //---- conversation_name ----
        conversation_name.setText("hoangtrung1801");
        conversation_name.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 14));
        add(conversation_name, "cell 0 1,grow");

        //======== panel4 ========
        {
            panel4.setLayout(new MigLayout(
                "filly,insets 0,hidemode 3,gap 2 0",
                // columns
                "[grow,fill]" +
                "[100,fill]",
                // rows
                "[grow]"));

            //======== panel5 ========
            {
                panel5.setLayout(new MigLayout(
                    "fill,insets 0,hidemode 3,gap 0 2",
                    // columns
                    "[fill]",
                    // rows
                    "[grow]" +
                    "[]" +
                    "[30]"));

                //======== scrollPane1 ========
                {
                    scrollPane1.setBorder(null);
                    scrollPane1.setBackground(Color.white);

                    //---- messagePane ----
                    messagePane.setBackground(Color.white);
                    messagePane.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    messagePane.setEditable(false);
                    scrollPane1.setViewportView(messagePane);
                }
                panel5.add(scrollPane1, "cell 0 0,grow");

                //======== panel6 ========
                {
                    panel6.setLayout(new MigLayout(
                        "insets 0,hidemode 3,gap 0 0",
                        // columns
                        "[fill]" +
                        "[fill]" +
                        "[fill]",
                        // rows
                        "[]"));

                    //---- fileBtn ----
                    fileBtn.setText("File");
                    fileBtn.addActionListener(e -> fileAction(e));
                    panel6.add(fileBtn, "cell 0 0");

                    //---- imageBtn ----
                    imageBtn.setText("Image");
                    imageBtn.addActionListener(e -> imageAction(e));
                    panel6.add(imageBtn, "cell 1 0");

                    //---- iconBtn ----
                    iconBtn.setText("Icon");
                    iconBtn.addActionListener(e -> iconAction(e));
                    panel6.add(iconBtn, "cell 2 0");
                }
                panel5.add(panel6, "cell 0 1");

                //---- input ----
                input.addActionListener(e -> textMessageAction(e));
                panel5.add(input, "cell 0 2,grow");

                //---- sendBtn ----
                sendBtn.setText("Send");
                sendBtn.addActionListener(e -> textMessageAction(e));
                panel5.add(sendBtn, "pad 0,cell 0 2,width 50:50,height 30:30,gapx 0 0,gapy 0 0");
            }
            panel4.add(panel5, "cell 0 0,growy");

            //======== scrollPane2 ========
            {
                scrollPane2.setBorder(null);

                //---- textPane2 ----
                textPane2.setBackground(Color.white);
                textPane2.setBorder(null);
                textPane2.setEditable(false);
                scrollPane2.setViewportView(textPane2);
            }
            panel4.add(scrollPane2, "cell 1 0,grow");
        }
        add(panel4, "cell 0 2,growy");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        if(targetUser != null)
            conversation_name.setText(targetUser.getUsername());
        if(conversation != null)
            conversation_name.setText(conversation.getConversationName());
        messageDocument = messagePane.getStyledDocument();
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private JLabel conversation_name;
    private JPanel panel4;
    private JPanel panel5;
    private JScrollPane scrollPane1;
    private JTextPane messagePane;
    private JPanel panel6;
    private JButton fileBtn;
    private JButton imageBtn;
    private JButton iconBtn;
    private JTextField input;
    private JButton sendBtn;
    private JScrollPane scrollPane2;
    private JTextPane textPane2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    public UserDto getTargetUser() {
        return targetUser;
    }

    public ChatGUI getChatGUI() {
        return chatGUI;
    }

    public ConversationDto getConversation() {
        return conversation;
    }

    public List<GroupMemberDto> getGroupMemberList() {
        return groupMemberList;
    }

    public void setGroupMemberList(List<GroupMemberDto> groupMemberList) {
        this.groupMemberList = groupMemberList;
    }

    public ConversationTabListener getListener() {
        return listener;
    }

    public void setListener(ConversationTabListener listener) {
        this.listener = listener;
    }
}
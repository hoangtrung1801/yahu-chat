/*
 * Created by JFormDesigner on Tue Jul 05 10:54:47 ICT 2022
 */

package client.components;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileView;
import javax.swing.text.*;

import client.ChatClient;
import client.ChatGUI;
import client.emojipicker.EmojiPicker;
import dto.*;
import model.Message;
import net.miginfocom.swing.*;
import org.imgscalr.Scalr;
import org.modelmapper.ModelMapper;
import shared.Helper;
import utility.MessageAttributeSet;

/**
 * @author unknown
 */
public class ConversationTab extends JPanel {
    public static void main(String[] args) {
        JFrame frame = new JFrame();

        ConversationTab conversationTab = new ConversationTab();

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

    public void appendTextMessage(MessageDto message) {
        try {
            SimpleAttributeSet styleUsername;
            if(message.getUser().getUsername().equals(ChatClient.user.getUsername())) {
                // if being current user
                styleUsername = MessageAttributeSet.getAttrForUsername();
                messageDocument.insertString(messageDocument.getLength(), message.getUser().getUsername() + " (you): ", styleUsername);
            } else {
                // target user
                styleUsername = MessageAttributeSet.getAttrForTargetUsername();
                messageDocument.insertString(messageDocument.getLength(), message.getUser().getUsername() + ": ", styleUsername);
            }
            messageDocument.insertString(messageDocument.getLength(), message.getMessageText(), MessageAttributeSet.getAttrForMessageText());
            insertEndlineDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void appendImage(ImageMessageDto message) {
        try {
            message.setMessageText("Sent image");
            appendTextMessage(message);

            Style style = messageDocument.addStyle("image", null);

            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setAlignment(attr, StyleConstants.ALIGN_CENTER);
            ImageIcon imageIcon = new ImageIcon(Scalr.resize(message.getImage(), 300));
            StyleConstants.setIcon(style, imageIcon);

            messageDocument.insertString(messageDocument.getLength(), "\t", style);
            insertEndlineDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void appendFile(FileMessageDto message) {
        try {
            SimpleAttributeSet styleUsername;
            if(message.getUser().getUsername().equals(ChatClient.user.getUsername())) {
                // if being current user
                styleUsername = MessageAttributeSet.getAttrForUsername();
                messageDocument.insertString(messageDocument.getLength(), message.getUser().getUsername() + " (you): ", styleUsername);
            } else {
                // target user
                styleUsername = MessageAttributeSet.getAttrForTargetUsername();
                messageDocument.insertString(messageDocument.getLength(), message.getUser().getUsername() + ": ", styleUsername);
            }
//            messageDocument.insertString(messageDocument.getLength(), message.getFilename(), MessageAttributeSet.getAttrForMessageText());
            FileLabel fileLabel = new FileLabel(message.getFilename());
            messagePane.insertComponent(fileLabel);
            insertEndlineDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertEndlineDocument() throws BadLocationException {
        messageDocument.insertString(messageDocument.getLength(), "\n", null);
    }

    // ------------------ ACTION -----------------------

    private void fileAction() {
        JFileChooser fileChooser = new JFileChooser();

        if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            listener.sendFileMessage(file);
        }
    }

    private void imageAction() {
        if(listener == null) {
            System.out.println("Listener is null ?? ");
        }
        
        JFileChooser fileChooser = new FileChooserImage();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if(fileChooser.showOpenDialog(this) == JFileChooser.FILES_ONLY) {
            File fileChosen = fileChooser.getSelectedFile();
            listener.sendImageMessage(fileChosen);
        }
    }

    private void textMessageAction() {
        if(listener == null) {
            System.out.println("Listener is null ?? ");
        }
        String text = input.getText();
        input.setText("");
        listener.sendTextMessage(text);
    }

    private void iconAction() {
        EmojiPicker emojiPicker = new EmojiPicker();
        emojiPicker.setClickListener(emoji -> {
            input.setText(input.getText() + emoji.getUnicode());
        });
    }

    private void videoCallAction(MouseEvent e) {
        ChatClient.clientGUI.controller.callVideoInConversation(conversation);
    }

    protected void requestDownloadFile(String filename) {
        List<String> filenameSplitted = Arrays.stream(filename.split("\\\\")).toList();
        String filenameOut = filenameSplitted.get(filenameSplitted.size() - 1);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if(fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            ChatClient.connection.sendRequestDownloadFile(filename, file.getAbsolutePath() + "/" + filenameOut);
        }
    }

    // ---------------------------------------------------
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel1 = new JPanel();
        videoCallBtn = new JPanel();
        label1 = new JLabel();
        label2 = new JLabel();
        conversation_name = new JLabel();
        panel4 = new JPanel();
        panel5 = new JPanel();
        scrollPane1 = new JScrollPane();
        messagePane = new JTextPane();
        panel6 = new JPanel();
        fileBtn = new JLabel();
        imageBtn = new JLabel();
        iconBtn = new JLabel();
        input = new JTextField();
        sendBtn = new JButton();
        scrollPane2 = new JScrollPane();
        textPane2 = new JTextPane();

        //======== this ========
        setFont(new Font("Yu Gothic UI", Font.BOLD, 12));
        setLayout(new MigLayout(
            "insets 0,hidemode 3",
            // columns
            "[grow,fill]",
            // rows
            "[60]" +
            "[24]" +
            "[grow]"));

        //======== panel1 ========
        {
            panel1.setLayout(new MigLayout(
                "insets 0 null 0 null,hidemode 3,aligny center",
                // columns
                "[fill]" +
                "[79,fill]",
                // rows
                "[]"));

            //======== videoCallBtn ========
            {
                videoCallBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                videoCallBtn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        videoCallAction(e);
                    }
                });
                videoCallBtn.setLayout(new MigLayout(
                    "insets 0,hidemode 3,aligny center,gapy 0",
                    // columns
                    "[fill]",
                    // rows
                    "[]" +
                    "[]"));

                //---- label1 ----
                label1.setIcon(new ImageIcon(getClass().getResource("/assets/video-call-icon.png")));
                videoCallBtn.add(label1, "cell 0 0,alignx center,growx 0");

                //---- label2 ----
                label2.setText("Video call");
                label2.setFont(new Font("Yu Gothic UI", Font.BOLD, 12));
                videoCallBtn.add(label2, "cell 0 1");
            }
            panel1.add(videoCallBtn, "cell 0 0,alignx center,growx 0");
        }
        add(panel1, "cell 0 0,growy");

        //---- conversation_name ----
        conversation_name.setText("hoangtrung1801");
        conversation_name.setFont(new Font("Yu Gothic UI", Font.BOLD | Font.ITALIC, 14));
        add(conversation_name, "cell 0 1,grow");

        //======== panel4 ========
        {
            panel4.setBorder(new CompoundBorder(
                new TitledBorder(""),
                new EmptyBorder(2, 0, 2, 0)));
            panel4.setLayout(new MigLayout(
                "filly,insets 0,hidemode 3,gap 5 0",
                // columns
                "[grow,fill]" +
                "[100,fill]",
                // rows
                "[grow]"));

            //======== panel5 ========
            {
                panel5.setBorder(null);
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
                    messagePane.setFont(new Font("Yu Gothic Medium", Font.BOLD, 14));
                    messagePane.setEditable(false);
                    scrollPane1.setViewportView(messagePane);
                }
                panel5.add(scrollPane1, "cell 0 0,grow");

                //======== panel6 ========
                {
                    panel6.setLayout(new MigLayout(
                        "insets 05 0 5 0,hidemode 3,gap 10 0",
                        // columns
                        "[fill]" +
                        "[fill]" +
                        "[fill]",
                        // rows
                        "[]"));

                    //---- fileBtn ----
                    fileBtn.setIcon(new ImageIcon(getClass().getResource("/assets/attachment-icon.png")));
                    fileBtn.setIconTextGap(0);
                    fileBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    fileBtn.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            fileAction();
                        }
                    });
                    panel6.add(fileBtn, "cell 0 0");

                    //---- imageBtn ----
                    imageBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    imageBtn.setIcon(new ImageIcon(getClass().getResource("/assets/image-icon.png")));
                    imageBtn.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            imageAction();
                        }
                    });
                    panel6.add(imageBtn, "cell 1 0");

                    //---- iconBtn ----
                    iconBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    iconBtn.setIcon(new ImageIcon(getClass().getResource("/assets/icon-icon.png")));
                    iconBtn.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            iconAction();
                        }
                    });
                    panel6.add(iconBtn, "cell 2 0");
                }
                panel5.add(panel6, "cell 0 1");

                //---- input ----
                input.setFont(new Font("Yu Gothic UI", Font.PLAIN, 12));
                input.addActionListener(e -> textMessageAction());
                panel5.add(input, "cell 0 2,grow,gapx null 5");

                //---- sendBtn ----
                sendBtn.setIconTextGap(0);
                sendBtn.setText("Send");
                sendBtn.setFont(new Font("Yu Gothic UI", Font.PLAIN, 12));
                sendBtn.addActionListener(e -> textMessageAction());
                panel5.add(sendBtn, "pad 0,cell 0 2,growy,height 30:30,gapx 0 0,gapy 0 0");
            }
            panel4.add(panel5, "cell 0 0,growy");

            //======== scrollPane2 ========
            {
                scrollPane2.setBorder(null);

                //---- textPane2 ----
                textPane2.setBackground(Color.white);
                textPane2.setBorder(null);
                textPane2.setEditable(false);
                textPane2.setFont(new Font("Yu Gothic UI", Font.PLAIN, 12));
                scrollPane2.setViewportView(textPane2);
            }
            panel4.add(scrollPane2, "cell 1 0,grow");
        }
        add(panel4, "cell 0 2,growy");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        if(targetUser != null)
            conversation_name.setText(targetUser.getUsername());
        if(conversation != null) {
            ModelMapper modelMapper = new ModelMapper();
            conversation_name.setText(Helper.getConversationNameFromConversation(conversation, modelMapper.map(ChatClient.user, UserDto.class)));
        }

        messageDocument = messagePane.getStyledDocument();

        DefaultCaret caret = (DefaultCaret) messagePane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private JPanel videoCallBtn;
    private JLabel label1;
    private JLabel label2;
    private JLabel conversation_name;
    private JPanel panel4;
    private JPanel panel5;
    private JScrollPane scrollPane1;
    private JTextPane messagePane;
    private JPanel panel6;
    private JLabel fileBtn;
    private JLabel imageBtn;
    private JLabel iconBtn;
    private JTextField input;
    private JButton sendBtn;
    private JScrollPane scrollPane2;
    private JTextPane textPane2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private class  FileLabel extends JLabel {
        private String filename;
        public FileLabel() {}

        public FileLabel(String filename) {
            super();
            this.filename = filename;

            List<String> filenameSplitted = Arrays.stream(filename.split("\\\\")).toList();
            setText("<HTML><u style=\"font-style: italic; color: #808080\">" + filenameSplitted.get(filenameSplitted.size()-1) + "</u></HTML>");

            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setAlignmentY(0.85f);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    requestDownloadFile(filename);
                }
            });
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }
    }


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

    public JTextPane getMessagePane() {
        return messagePane;
    }
}
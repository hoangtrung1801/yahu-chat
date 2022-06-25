package cclient;

import dao.ConversationDAO;
import dao.implement.ConversationDAOImpl;
import model.Conversation;
import model.GroupMember;
import model.User;

import javax.swing.*;
import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class ChatGUIController {

    private ConversationDAO conversationDAO;

    private User targetUser;
    private Conversation conversation;

    public ChatGUI gui;

    public ChatGUIController(ChatGUI gui, User targetUser) {
        this.gui = gui;
        this.targetUser = targetUser;

        this.conversationDAO = new ConversationDAOImpl();

        // init conversation
        conversation = conversationDAO.findConversationWithUsers(
                ChatClient.user,
                targetUser
        );
        if(conversation == null) {
            Conversation newConversation = new Conversation();
            newConversation.setConversationName(ChatClient.user.getUsername() + " and " + targetUser.getUsername());
            conversation = conversationDAO.create(newConversation);

            GroupMember gmUser = new GroupMember(ChatClient.user, conversation, Instant.now(), null);
            GroupMember gmTargetUser = new GroupMember(targetUser, conversation, Instant.now(), null);

            conversation.setGroupMembers(new HashSet<>(Arrays.asList(gmUser, gmTargetUser)));
            conversation = conversationDAO.update(conversation);
        }
    }

    // -------------------------- Action --------------------------
    public void sendTextMessage() {
        String textMessage = gui.inputField.getText();
        gui.inputField.setText("");

        ChatClient.connection.sendMessageInConversation(conversation, textMessage);
    }

    public void sendFileMessage() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            File fileChosen;
            if(fileChooser.showOpenDialog(gui.panel) == JFileChooser.APPROVE_OPTION) {
                fileChosen = fileChooser.getSelectedFile();
            }
//            client.clientConnection.sendFileMessage(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------
    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
}

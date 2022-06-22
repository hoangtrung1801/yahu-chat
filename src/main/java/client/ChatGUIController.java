package client;

import model.Conversation;
import model.GroupMember;
import model.GroupMemberId;
import model.User;
import utilities.Helper;

import javax.swing.*;
import java.io.File;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;

public class ChatGUIController {

    private User targetUser;
    private Conversation conversation;
    ChatGUI gui;

    public ChatGUIController(ChatGUI gui, User targetUser) {
        this.gui = gui;
        this.targetUser = targetUser;

        // init conversation
        conversation = ApplicationContext.getConversationDAO().findConversationWithUsers(
                ApplicationContext.getUser(),
                targetUser
        );
        if(conversation == null) {
            Conversation newConversation = new Conversation();
            newConversation.setConversationName(ApplicationContext.getUser().getUsername() + " and " + targetUser.getUsername());
            conversation = ApplicationContext.getConversationDAO().create(newConversation);

            GroupMember gmUser = new GroupMember(ApplicationContext.getUser(), conversation, Instant.now(), null);
            GroupMember gmTargetUser = new GroupMember(targetUser, conversation, Instant.now(), null);

            conversation.setGroupMembers(new HashSet<>(Arrays.asList(gmUser, gmTargetUser)));
            conversation = ApplicationContext.getConversationDAO().update(conversation);
        }
    }
    // -------------------------- Action --------------------------
    public void sendTextMessage() {
        String textMessage = gui.inputField.getText();
        gui.inputField.setText("");

        ApplicationContext.getClientConnection().sendTextMessage(conversation, textMessage);
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

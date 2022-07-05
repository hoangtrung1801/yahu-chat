/*
 * Created by JFormDesigner on Sun Jul 03 15:38:40 ICT 2022
 */

package client.components;

import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;

import client.ChatClient;
import dto.ConversationDto;
import dto.UserDto;
import model.Conversation;
import net.miginfocom.swing.*;
import org.imgscalr.Scalr;

import java.awt.event.MouseAdapter;

/**
 * @author unknown
 */
public class ConversationCell extends JPanel {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new ConversationCell());

        frame.setVisible(true);
        frame.pack();;
    }

    private UserDto targetUser;
    private ConversationDto conversation;

    public ConversationCell(UserDto targetUser) {
        this.targetUser = targetUser;
        initComponents();

        username.setText(targetUser.getUsername());
    }

    public ConversationCell(ConversationDto conversation) {
        this.conversation = conversation;
        initComponents();
        username.setText(conversation.getConversationName());
    }

    public ConversationCell() {
        initComponents();
    }

    private void hoverOn(MouseEvent e) {
        setBackground(Color.LIGHT_GRAY);
    }

    private void hoverOff(MouseEvent e) {
        setBackground(null);
    }

    private void openConversation(MouseEvent e) {
        ChatClient.clientGUI.controller.openChatGUIWithConversation(conversation);
    }

    public UserDto getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(UserDto targetUser) {
        this.targetUser = targetUser;
    }
        public ConversationDto getConversation() {
        return conversation;
    }

    public void setConversation(ConversationDto conversation) {
        this.conversation = conversation;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        label1 = new JLabel();
        username = new JLabel();

        //======== this ========
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openConversation(e);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                hoverOn(e);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                hoverOff(e);
            }
        });
        setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[fill]",
            // rows
            "[]"));

        //---- label1 ----
        label1.setText("text");
        label1.setIcon(new ImageIcon(getClass().getResource("/assets/user-icon.png")));
        add(label1, "cell 0 0,width 20:20:20,height 20:20:20");

        //---- username ----
        username.setText("hoangtrung1801");
        add(username, "cell 1 0");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label1;
    private JLabel username;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}

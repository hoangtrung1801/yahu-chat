/*
 * Created by JFormDesigner on Sun Jul 03 15:38:40 ICT 2022
 */

package client.components;

import client.ChatClient;
import dto.ConversationDto;
import dto.UserDto;
import net.miginfocom.swing.MigLayout;
import org.modelmapper.ModelMapper;
import shared.Helper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author unknown
 */
public class ConversationCell extends JPanel {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new ConversationCell());

        ConversationCell cell = new ConversationCell();
        cell.markNotRead();
        frame.add(cell);

        frame.setVisible(true);
        frame.pack();;
    }

    private UserDto targetUser;
    private ConversationDto conversation;

    public ConversationCell(ConversationDto conversation) {
        this.conversation = conversation;
        initComponents();

        // conversation name
        // if having only 2 user, get specific name
        ModelMapper modelMapper = new ModelMapper();
        username.setText(Helper.getConversationNameFromConversation(conversation, modelMapper.map(ChatClient.user, UserDto.class)));
    }

    public ConversationCell() {
        initComponents();
    }

    public void markNotRead() {
        username.setForeground(Color.RED);
        dotIsRead.setVisible(true);
        updateUI();
    }

    public void markRead() {
        username.setForeground(null);
        dotIsRead.setVisible(false);
        updateUI();
    }

    private void hoverOn(MouseEvent e) {
        setBackground(Color.LIGHT_GRAY);
    }

    private void hoverOff(MouseEvent e) {
        setBackground(null);
    }

    private void openConversation(MouseEvent e) {
        ChatClient.clientGUI.controller.openChatGUIWithConversation(conversation);
        markRead();
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
        dotIsRead = new DotIsRead();

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
        add(dotIsRead, "cell 2 0");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label1;
    private JLabel username;
    private DotIsRead dotIsRead;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private class DotIsRead extends JPanel {
        public DotIsRead() {
            super();
            setVisible(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.red);
            g2.fillOval(0, 0, 6, 6);
        }

        private void initComponents() {
            // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents

            //======== this ========
            setLayout(null);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < getComponentCount(); i++) {
                    Rectangle bounds = getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                setMinimumSize(preferredSize);
                setPreferredSize(preferredSize);
            }
            // JFormDesigner - End of component initialization  //GEN-END:initComponents
        }
    }
}

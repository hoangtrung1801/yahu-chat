package client;

import client.components.ConversationTab;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class ChatGUI extends JFrame {

    public static void main(String[] args) {
        FlatLightLaf.setup();
        ChatGUI chatGUI = new ChatGUI();

        chatGUI.tabConversation.addTab("message", new ConversationTab());

        chatGUI.setVisible(true);
    }

    public ChatGUIController controller;

    public ChatGUI() {
        controller = new ChatGUIController(this);
        init();
    }

    public void requestFocusInTab(ConversationTab tab) {
        // if conversation tab have already opened
        tabConversation.setSelectedIndex(controller.conversationTabs.indexOf(tab));
        tabConversation.requestFocus();
    }

    public void init() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        tabConversation = new JTabbedPane();

        //======== this ========
        setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
        var contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "fill,hidemode 3",
            // columns
            "[fill, grow]",
            // rows
            "[grow]"));

        //======== tabConversation ========
        {
            tabConversation.setTabPlacement(SwingConstants.BOTTOM);
        }
        contentPane.add(tabConversation, "cell 0 0,growy");
        setSize(550, 550);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        setTitle("Conversation");
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JTabbedPane tabConversation;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public JTabbedPane getTabConversation() {return  tabConversation;}
}

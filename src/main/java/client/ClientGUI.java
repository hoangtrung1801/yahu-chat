package client;

import java.awt.event.*;
import client.components.ConversationCell;
import dto.ConversationDto;
import dto.UserDto;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClientGUI extends JFrame {
    public static void main(String[] args) {
        ClientGUI gui = new ClientGUI();

        for(int i=0;i<2;i++) {
            ConversationCell cell = new ConversationCell();
            gui.listConversationPane.add(cell);
        }

        gui.setVisible(true);
    }

    public ClientGUIController controller;
    private NewContactGUI newContactGUI;
    private NewGroupUsersGUI newGroupUsersGUI;

    public List<ConversationCell> listConversationCells;

    public ClientGUI() {
        this.controller = new ClientGUIController(this);
        initComponents();

        setVisible(true);
        setTitle(ChatClient.user.getUsername());
        username.setText(ChatClient.user.getUsername());
    }

    private void closeApp(WindowEvent e) {
        if (JOptionPane.showConfirmDialog(this,
                "Are you sure you want to close this window?", "Close Window?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }
    //    // --------------------------------------------------
    private void addContactAction(MouseEvent e) {
        if(newContactGUI == null)
            newContactGUI = new NewContactGUI();
        newContactGUI.setVisible(true);
    }

    private void addGroupUsersAction() {
        if(newGroupUsersGUI == null) newGroupUsersGUI = new NewGroupUsersGUI();
        newGroupUsersGUI.setVisible(true);
    }

    public void updateListConversations(List<ConversationDto> conversations) {
        this.listConversationCells = new ArrayList<>();
        listConversationPane.removeAll();
        for(ConversationDto conversation: conversations) {
            ConversationCell cell = new ConversationCell(conversation);
            listConversationPane.add(cell);
            listConversationCells.add(cell);
        }
        listConversationPane.updateUI();
    }

    public ConversationCell findConversationCell(ConversationDto conversation) {
        for(var cell : listConversationCells) {
            if(cell.getConversation().equals(conversation)) return cell;
        }
        return null;
    }


    // -------------------- Design ---------------------------------
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        userPane = new JPanel();
        userImage = new JLabel();
        username = new JLabel();
        actionPane = new JPanel();
        addContactBtn = new JPanel();
        label1 = new JLabel();
        label2 = new JLabel();
        addGroupUsersBtn = new JPanel();
        label3 = new JLabel();
        label4 = new JLabel();
        listConversationScrollPane = new JScrollPane();
        listConversationPane = new JPanel();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                closeApp(e);
            }
            @Override
            public void windowClosing(WindowEvent e) {
                closeApp(e);
            }
        });
        var contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[grow,fill]",
            // rows
            "[50:n]" +
            "[]" +
            "[grow]"));

        //======== userPane ========
        {
            userPane.setLayout(new MigLayout(
                "insets 0,hidemode 3",
                // columns
                "[fill]" +
                "[fill]",
                // rows
                "[]"));

            //---- userImage ----
            userImage.setText("text");
            userImage.setIcon(new ImageIcon(getClass().getResource("/assets/user-icon.png")));
            userPane.add(userImage, "cell 0 0,width 50:50:50,height 50:50:50");

            //---- username ----
            username.setText("hoangtrung1801");
            username.setFont(new Font("Segoe UI", Font.BOLD, 12));
            userPane.add(username, "cell 1 0,aligny top,growy 0");
        }
        contentPane.add(userPane, "cell 0 0");

        //======== actionPane ========
        {
            actionPane.setLayout(new MigLayout(
                "insets 0,hidemode 3,gapx 16",
                // columns
                "[fill]",
                // rows
                "[fill]"));

            //======== addContactBtn ========
            {
                addContactBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                addContactBtn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        addContactAction(e);
                    }
                });
                addContactBtn.setLayout(new MigLayout(
                    "hidemode 3,align center center,gap 0 0, insets 0",
                    // columns
                    "[fill]",
                    // rows
                    "[]" +
                    "[]"));

                //---- label1 ----
                label1.setIcon(new ImageIcon(getClass().getResource("/assets/add-user.png")));
                addContactBtn.add(label1, "cell 0 0,align center center,grow 0 0");

                //---- label2 ----
                label2.setText("Add Friend");
                addContactBtn.add(label2, "cell 0 1,alignx center,growx 0");
            }
            actionPane.add(addContactBtn, "cell 0 0");

            //======== addGroupUsersBtn ========
            {
                addGroupUsersBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                addGroupUsersBtn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        addGroupUsersAction();
                    }
                });
                addGroupUsersBtn.setLayout(new MigLayout(
                    "hidemode 3,align center center,gap 0 0, insets 0",
                    // columns
                    "[fill]",
                    // rows
                    "[]" +
                    "[]"));

                //---- label3 ----
                label3.setIcon(new ImageIcon(getClass().getResource("/assets/add-group-users.png")));
                addGroupUsersBtn.add(label3, "cell 0 0,align center center,grow 0 0");

                //---- label4 ----
                label4.setText("Add Group");
                addGroupUsersBtn.add(label4, "cell 0 1,alignx center,growx 0");
            }
            actionPane.add(addGroupUsersBtn, "cell 1 0");
        }
        contentPane.add(actionPane, "cell 0 1");

        //======== listConversationScrollPane ========
        {
            listConversationScrollPane.setBorder(null);
            listConversationScrollPane.setPreferredSize(new Dimension(300, 20));

            //======== listConversationPane ========
            {
                listConversationPane.setLayout(new MigLayout(
                    "flowy,insets 0,hidemode 3",
                    // columns
                    "[grow,fill]",
                    // rows
                    "[]" +
                    "[]"));
            }
            listConversationScrollPane.setViewportView(listConversationPane);
        }
        contentPane.add(listConversationScrollPane, "cell 0 2,aligny top,growy 1,hmin 0");
        setSize(300, 400);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel userPane;
    private JLabel userImage;
    private JLabel username;
    private JPanel actionPane;
    private JPanel addContactBtn;
    private JLabel label1;
    private JLabel label2;
    private JPanel addGroupUsersBtn;
    private JLabel label3;
    private JLabel label4;
    private JScrollPane listConversationScrollPane;
    private JPanel listConversationPane;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    public NewContactGUI getNewContactGUI() {return newContactGUI;};

    public NewGroupUsersGUI getNewGroupUsersGUI() {
        return newGroupUsersGUI;
    }
}

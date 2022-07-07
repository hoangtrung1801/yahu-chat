package client;

import java.awt .*;
import java.awt.*;
import java.awt.event .*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing .*;
import javax.swing.*;

import dto.UserDto;
import net.miginfocom.swing .*;
import net.miginfocom.swing.*;
import org.modelmapper.ModelMapper;

public class NewGroupUsersGUI extends JFrame {

    public static void main(String[] args) {
        new NewGroupUsersGUI().setVisible(true);
    }

    private List<UserDto> listUsers;

    public NewGroupUsersGUI() {
        listUsers = new ArrayList<>();
        initComponents();
    }

    public void foundUsers(List<UserDto> users) {
        newUserContainer.removeAll();
        for(var user: users) {
            NewContactItem item = new NewContactItem(user);
            newUserContainer.add(item);
        }
        newUserContainer.updateUI();
    }

    public void addNewUserInGroup(UserDto user) {
        if(listUsers.contains(user)) return;
        listUsers.add(user);

        // update ui
        UserInGroupItem userInGroupItem = new UserInGroupItem(user);
        listUsersInGroup.add(userInGroupItem);
        listUsersInGroup.updateUI();
    }

    public void removeUserInGroup(UserDto user) {
        if(!listUsers.contains(user)) return;
        listUsers.remove(user);

        // update ui
        listUsersInGroup.removeAll();;
        for(var userInGroup: listUsers) {
            UserInGroupItem userInGroupItem = new UserInGroupItem(userInGroup);
            listUsersInGroup.add(userInGroupItem);
        }
        listUsersInGroup.updateUI();
    }

    private void findUser() {
        String target = input.getText();
        ChatClient.connection.sendFindUserNewGroup(target);
    }

    private void confirmAddNewGroup() {
        ModelMapper modelMapper = new ModelMapper();
        String conversationName = lConversationName.getText();

        listUsers.add(modelMapper.map(ChatClient.user, UserDto.class));
        ChatClient.connection.sendNewConversationWithMutilUser(listUsers, conversationName);

        JOptionPane.showMessageDialog(null, "Create group user successfull");
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        label1 = new JLabel();
        panel2 = new JPanel();
        label5 = new JLabel();
        lConversationName = new JTextField();
        label2 = new JLabel();
        panel1 = new JPanel();
        input = new JTextField();
        button1 = new JButton();
        scrollPane1 = new JScrollPane();
        newUserContainer = new JPanel();
        label3 = new JLabel();
        scrollPane2 = new JScrollPane();
        listUsersInGroup = new JPanel();
        addNewGroupBtn = new JButton();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[grow,fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[grow]" +
            "[]" +
            "[grow]" +
            "[]"));

        //---- label1 ----
        label1.setText("Add new group");
        label1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        contentPane.add(label1, "cell 0 0,alignx center,growx 0");

        //======== panel2 ========
        {
            panel2.setLayout(new MigLayout(
                "insets 0,hidemode 3",
                // columns
                "[fill]" +
                "[grow,fill]",
                // rows
                "[]"));

            //---- label5 ----
            label5.setText("Name:");
            panel2.add(label5, "cell 0 0");
            panel2.add(lConversationName, "cell 1 0");
        }
        contentPane.add(panel2, "cell 0 1");

        //---- label2 ----
        label2.setText("Find users: ");
        contentPane.add(label2, "cell 0 2");

        //======== panel1 ========
        {
            panel1.setLayout(new MigLayout(
                "insets 0 20 0 20,hidemode 3",
                // columns
                "[grow,fill]" +
                "[fill]",
                // rows
                "[]"));
            panel1.add(input, "cell 0 0");

            //---- button1 ----
            button1.setText("Find");
            button1.addActionListener(e -> findUser());
            panel1.add(button1, "cell 1 0");
        }
        contentPane.add(panel1, "cell 0 3");

        //======== scrollPane1 ========
        {
            scrollPane1.setBorder(null);

            //======== newUserContainer ========
            {
                newUserContainer.setLayout(new MigLayout(
                    "flowy,insets 0 20 0 20,hidemode 3,aligny top",
                    // columns
                    "[grow,fill]",
                    // rows
                    "[]"));
            }
            scrollPane1.setViewportView(newUserContainer);
        }
        contentPane.add(scrollPane1, "cell 0 4,growy");

        //---- label3 ----
        label3.setText("Users in  group:");
        contentPane.add(label3, "cell 0 5");

        //======== scrollPane2 ========
        {
            scrollPane2.setBorder(null);

            //======== listUsersInGroup ========
            {
                listUsersInGroup.setLayout(new MigLayout(
                    "flowy,insets 0 20 0 20,hidemode 3,aligny top",
                    // columns
                    "[grow,fill]",
                    // rows
                    "[]"));
            }
            scrollPane2.setViewportView(listUsersInGroup);
        }
        contentPane.add(scrollPane2, "cell 0 6,growy");

        //---- addNewGroupBtn ----
        addNewGroupBtn.setText("Confirm");
        addNewGroupBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                confirmAddNewGroup();
            }
        });
        contentPane.add(addNewGroupBtn, "cell 0 7,alignx center,growx 0");
        setSize(300, 400);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label1;
    private JPanel panel2;
    private JLabel label5;
    private JTextField lConversationName;
    private JLabel label2;
    private JPanel panel1;
    private JTextField input;
    private JButton button1;
    private JScrollPane scrollPane1;
    private JPanel newUserContainer;
    private JLabel label3;
    private JScrollPane scrollPane2;
    private JPanel listUsersInGroup;
    private JButton addNewGroupBtn;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
    private class NewContactItem extends JPanel {
        private UserDto user;

        private NewContactItem() {
            initComponents();
        }

        private NewContactItem(UserDto user) {
            initComponents();

            this.user = user;
            username.setText(user.getUsername());
        }

        private void addUserAction() {
            addNewUserInGroup(user);
        }

        private void initComponents() {
            // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
            username = new JLabel();
            addUserBtn = new JButton();

            //======== this ========
            setLayout(new MigLayout(
                "insets 0,hidemode 3",
                // columns
                "[grow,fill]" +
                "[fill]",
                // rows
                "[]"));

            //---- username ----
            username.setText("hoangtrung1801");
            add(username, "cell 0 0");

            //---- addUserBtn ----
            addUserBtn.setText("ADD");
            addUserBtn.addActionListener(e -> addUserAction());
            add(addUserBtn, "cell 1 0");
            // JFormDesigner - End of component initialization  //GEN-END:initComponents
        }

        // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
        private JLabel username;
        private JButton addUserBtn;
        // JFormDesigner - End of variables declaration  //GEN-END:variables
    }

    private class UserInGroupItem extends JPanel {

        private UserDto user;

        private UserInGroupItem() {
            initComponents();
        }

        private UserInGroupItem(UserDto user) {
            this.user = user;
            initComponents();

            usernameInNewUser.setText(user.getUsername());
        }

        private void removeUserInGroupAction() {
            removeUserInGroup(user);
        }

        private void initComponents() {
            // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
            label4 = new JLabel();
            usernameInNewUser = new JLabel();
            button2 = new JButton();

            //======== this ========
            setLayout(new MigLayout(
                "insets 0,hidemode 3",
                // columns
                "[fill]" +
                "[grow,fill]" +
                "[fill]",
                // rows
                "[]"));

            //---- label4 ----
            label4.setText("+");
            add(label4, "cell 0 0");

            //---- usernameInNewUser ----
            usernameInNewUser.setText("text");
            add(usernameInNewUser, "cell 1 0");

            //---- button2 ----
            button2.setText("X");
            button2.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    removeUserInGroupAction();
                }
            });
            add(button2, "cell 2 0,width 30:30");
            // JFormDesigner - End of component initialization  //GEN-END:initComponents
        }

        // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
        private JLabel label4;
        private JLabel usernameInNewUser;
        private JButton button2;
        // JFormDesigner - End of variables declaration  //GEN-END:variables
    }
}




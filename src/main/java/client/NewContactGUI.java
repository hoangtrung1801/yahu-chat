/*
 * Created by JFormDesigner on Wed Jul 06 11:07:31 ICT 2022
 */

package client;

import dto.UserDto;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * @author unknown
 */
public class NewContactGUI extends JFrame {
    public static void main(String[] args) {
        NewContactGUI gui = new NewContactGUI();
        gui.newContactContainer.add(new NewContactGUI.NewContactItem());
        gui.setVisible(true);
    }

    public NewContactGUI() {
        initComponents();
    }

    private void findContact(ActionEvent e) {
        String target = input.getText();
        ChatClient.connection.sendFindContact(target);
    }

    public void foundContact(List<UserDto> users) {
        newContactContainer.removeAll();
        for(var user: users) {
            NewContactItem item = new NewContactItem(user);
            newContactContainer.add(item);
        }
        newContactContainer.updateUI();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        label1 = new JLabel();
        panel1 = new JPanel();
        input = new JTextField();
        button1 = new JButton();
        scrollPane1 = new JScrollPane();
        newContactContainer = new JPanel();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[grow,fill]",
            // rows
            "[]" +
            "[]" +
            "[grow]"));

        //---- label1 ----
        label1.setText("Add new contact");
        label1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        contentPane.add(label1, "cell 0 0,alignx center,growx 0");

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
            button1.addActionListener(e -> findContact(e));
            panel1.add(button1, "cell 1 0");
        }
        contentPane.add(panel1, "cell 0 1");

        //======== scrollPane1 ========
        {
            scrollPane1.setBorder(null);

            //======== newContactContainer ========
            {
                newContactContainer.setLayout(new MigLayout(
                    "flowy,insets 0 20 0 20,hidemode 3,aligny top",
                    // columns
                    "[grow,fill]",
                    // rows
                    "[]"));
            }
            scrollPane1.setViewportView(newContactContainer);
        }
        contentPane.add(scrollPane1, "cell 0 2,growy");
        setSize(300, 400);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label1;
    private JPanel panel1;
    private JTextField input;
    private JButton button1;
    private JScrollPane scrollPane1;
    private JPanel newContactContainer;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private static class NewContactItem extends JPanel {
        private UserDto user;
        private NewContactItem() {
            initComponents();
        }

        private NewContactItem(UserDto user) {
            initComponents();

            this.user = user;
            username.setText(user.getUsername());
        }

        private void addAction(ActionEvent e) {
            ChatClient.connection.sendNewConversationWithUser(user);
        }

        private void initComponents() {
            // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
            username = new JLabel();
            addBtn = new JButton();

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

            //---- addBtn ----
            addBtn.setText("ADD");
            addBtn.addActionListener(e -> addAction(e));
            add(addBtn, "cell 1 0");
            // JFormDesigner - End of component initialization  //GEN-END:initComponents
        }

        // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
        private JLabel username;
        private JButton addBtn;
        // JFormDesigner - End of variables declaration  //GEN-END:variables


        public JLabel getUsername() {
            return username;
        }

        public void setUsername(JLabel username) {
            this.username = username;
        }

        public JButton getAddBtn() {
            return addBtn;
        }

        public void setAddBtn(JButton addBtn) {
            this.addBtn = addBtn;
        }

        public UserDto getUser() {
            return user;
        }

        public void setUser(UserDto user) {
            this.user = user;
        }
    }
}

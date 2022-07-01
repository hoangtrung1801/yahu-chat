package client;

import dto.UserDto;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import utility.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class ClientGUI extends JFrame {
    public ClientGUIController controller;
    public Set<UserDto> listOnlineUsers;

    JPanel panel, userPanel, onlineUserPanel;
    JLabel lUsername;

    public ClientGUI() {
        this.controller = new ClientGUIController(this);

        MigLayout layout = new MigLayout(
                new LC().width(String.valueOf(Constants.CLIENT_GUI_WIDTH)).height(String.valueOf(Constants.CLIENT_GUI_HEIGHT)).debug(1)
        );
        panel = new JPanel(layout);

        initUserPanel();
        initOnlineUserPanel();

        setContentPane(panel);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setTitle(ChatClient.user.getUsername());


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(panel,
                        "Are you sure you want to close this window?", "Close Window?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    System.exit(0);
                }
            }
        });
    }

    // --------------------- initialize -------------------
    private void initUserPanel() {
        MigLayout layout = new MigLayout();
        userPanel = new JPanel(layout);

        ImageIcon userIcon = new ImageIcon(Objects.requireNonNull(ClientGUI.class.getResource("/assets/user-icon.png")));
        JLabel lUserIcon = new JLabel();
        lUserIcon.setIcon(new ImageIcon(userIcon.getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        userPanel.add(lUserIcon, new CC().dockWest());

        lUsername = new JLabel(ChatClient.user.getUsername());
        userPanel.add(lUsername);

        panel.add(userPanel, new CC().wrap().width("100%").height("150px"));
    }

    private void initOnlineUserPanel() {
        MigLayout layout = new MigLayout(new LC().debug(1));
        onlineUserPanel = new JPanel(layout);
        panel.add(onlineUserPanel, new CC().wrap().width("100%"));
    }


    // --------------------------------------------------
    public void updateOnlineUsersPanel(Set<UserDto> listOnlineUsers) {
        this.listOnlineUsers = listOnlineUsers;
        onlineUserPanel.removeAll();
        for(UserDto onlineUser : listOnlineUsers) {
            if(onlineUser.getId().equals(ChatClient.user.getId())) continue;
            OnlineUserCell cell = new OnlineUserCell(onlineUser);
            onlineUserPanel.add(cell, new CC().wrap().width("100%").height("70px"));
        }
        onlineUserPanel.updateUI();
    }

    class OnlineUserCell extends JPanel {

        public UserDto targetUser;

        public OnlineUserCell(UserDto targetUser) {
            this.targetUser = targetUser;

            MigLayout layout = new MigLayout("");
            setLayout(layout);

            JLabel lUsername = new JLabel(targetUser.getUsername());
            add(lUsername);

            ImageIcon userIcon = new ImageIcon(Objects.requireNonNull(ClientGUI.class.getResource("/assets/user-icon.png")));
            JLabel lUserIcon = new JLabel();
            lUserIcon.setIcon(new ImageIcon(userIcon.getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));

            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // hover
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(Color.lightGray);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(UIManager.getColor("Panel.background"));
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    controller.openChatGUIWithUser(targetUser);
                }
            });

            add(lUserIcon, new CC().dockWest().gapAfter("8px"));
        }
    }
}

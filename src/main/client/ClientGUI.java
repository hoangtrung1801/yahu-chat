package main.client;

import com.formdev.flatlaf.FlatLightLaf;
import main.model.User;
import main.utilities.Constants;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ClientGUI extends JFrame {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        new ClientGUI(null);
    }

    ChatClient client;

    JPanel panel, userPanel, onlineUserPanel;

    ArrayList<ChatGUI> chatManager;

    public ClientGUI(ChatClient client) {
        this.client = client;
        chatManager = new ArrayList<>();

        MigLayout layout = new MigLayout(
                new LC().width(String.valueOf(Constants.CLIENT_GUI_WIDTH)).height(String.valueOf(Constants.CLIENT_GUI_HEIGHT)).debug(1)
        );
        panel = new JPanel(layout);

        initUserPanel();
        initOnlineUserPanel();

        setContentPane(panel);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(Constants.GUI_WIDTH, Constants.GUI_HEIGHT);
        pack();
        setLocationRelativeTo(null);

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
        userPanel.setBackground(Color.black);

        panel.add(userPanel, new CC().wrap().width("100%").height("150px"));
    }

    private void initOnlineUserPanel() {
        MigLayout layout = new MigLayout(new LC().debug(1));
        onlineUserPanel = new JPanel(layout);

//        for(int i=0;i<4;i++) {
//            OnlineUserCell cell = new OnlineUserCell(new User(0, null));
//            onlineUserPanel.add(cell, new CC().wrap().width("100%").height("70px"));
//        }

        panel.add(onlineUserPanel, new CC().wrap().width("100%"));
    }



    // --------------------------------------------------
    public void updateOnlineUsersPanel() {
        onlineUserPanel.removeAll();
        for(User user : ApplicationContext.getClientConnection().onlineUsers) {
            if(user.getUserId() == ApplicationContext.getUser().getUserId()) continue;
            OnlineUserCell cell = new OnlineUserCell(user);
            onlineUserPanel.add(cell, new CC().wrap().width("100%").height("70px"));
        }
        onlineUserPanel.updateUI();
    }

    private void openChatGUIWithUser(User user) {
        ChatGUI chat = new ChatGUI(user);

        chatManager.add(chat);
        chat.setVisible(true);
    }

    class OnlineUserCell extends JPanel {

        public User user;

        public OnlineUserCell(User user) {
            this.user = user;

            MigLayout layout = new MigLayout("");
            setLayout(layout);

            JLabel lUsername = new JLabel(user.getUsername());
            add(lUsername);

            ImageIcon userIcon = new ImageIcon(getClass().getResource("/main/assets/user-icon.png"));
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
                    openChatGUIWithUser(user);
                }
            });

            add(lUserIcon, new CC().dockWest().gapAfter("8px"));
        }

    }
}

package client;

import model.User;

import javax.swing.*;
import java.io.File;

public class ChatGUIController {

    User targetUser;
    ChatGUI gui;

    public ChatGUIController(ChatGUI gui) {
        this.gui = gui;
        targetUser = gui.targetUser;
    }
    // -------------------------- Action --------------------------
    public void sendTextMessage() {
        String textMessage = gui.inputField.getText();
        gui.inputField.setText("");

        ApplicationContext.getClientConnection().sendTextMessage(targetUser, textMessage);
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

}

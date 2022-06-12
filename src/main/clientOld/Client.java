package main.clientOld;

import com.formdev.flatlaf.FlatIntelliJLaf;
import main.model.FileMessage;
import main.model.Message;
import main.utilities.Constants;
import main.utilities.MessageUtil;
import main.utilities.TextAttributeCustom;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class Client extends JFrame implements Runnable {

    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;

    JPanel panel, sendPanel, actionPanel;
    JTextPane messageArea;
    JScrollPane messageAreaScroll;
    JTextField inputField;
    JButton sendMessageBtn, sendFileBtn;

    Document messageDocument;

    @Override
    public void run() {
        SwingUtilities.invokeLater(() -> {
//            FlatLightLaf.setup();
            FlatIntelliJLaf.setup();
            createGUI();
        });
        init();
    }

    void init() {
        try {
            socket = new Socket("localhost", Constants.PORT);
            System.out.println("Client entered to server");

            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());

            notifyUserEntered();
            while (true) {
                while (ois.available() == 0) {
                    Thread.sleep(1);
                }

//                String typeMessage = ois.readUTF();
//                if (typeMessage.equals(Constants.SEND_MESSAGE)) {
//                    receiveMessage();
//                } else if (typeMessage.equals(Constants.NOTIFY_USER_ENTERED)) {
//                    String text = ois.readUTF();
//                    appendTextUserEntered(text);
//                } else if(typeMessage.equals(Constants.SEND_FILE)) {
//                    FileMessage fileMessage = (FileMessage) ois.readObject();
//                    appendFileMessageToGUI(fileMessage);
//                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                ois.close();
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void createGUI() {
        MigLayout layout = new MigLayout(
                "wrap, fill, debug",
                "[]",
                "[][][]"
        );
        panel = new JPanel(layout);

        // message area
        messageArea = new JTextPane();
        messageDocument = messageArea.getDocument();
        messageAreaScroll = new JScrollPane(messageArea);

        messageArea.setEditable(false);
        messageArea.setBackground(Color.white);

        panel.add(messageAreaScroll, new CC().width(String.valueOf(Constants.GUI_WIDTH)).height(String.valueOf(Constants.GUI_HEIGHT * 0.9)));


        // action panel
        actionPanel = new JPanel(new MigLayout("ins 0"));

        sendFileBtn = new JButton("File");
        sendFileBtn.addActionListener(new sendFileAction());
        actionPanel.add(sendFileBtn);

        panel.add(actionPanel);

        //
        sendPanel = new JPanel(new MigLayout("ins 0"));

        // Input field
        inputField = new JTextField();
        inputField.setPreferredSize(new Dimension((int) (Constants.GUI_WIDTH * 0.95), (int) (Constants.GUI_HEIGHT * 0.1)));
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        sendPanel.add(inputField, new CC().growX().height("100%"));

        // send button
        ImageIcon sendIcon = new ImageIcon("src/main/assets/send-icon.png");
        sendMessageBtn = new JButton("SEND");
        sendMessageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        sendPanel.add(sendMessageBtn, new CC().height("100%"));
        panel.add(sendPanel, new CC().width("100%").height("32px"));

        setContentPane(panel);

        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setSize(Constants.GUI_WIDTH, Constants.GUI_HEIGHT);
        pack();
        setLocationRelativeTo(null);
    }

    void notifyUserEntered() {
        try {
            oos.writeUTF(Constants.NOTIFY_USER_ENTERED);
            oos.flush();

            oos.writeObject(ApplicationContext.getUser());
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void appendMessageToGUI(Message message) {
        try {
            if (messageArea != null) {
                // [hh:mm] username > text
                ArrayList<String> messageComps = MessageUtil.getMessageStringComps(message);
                messageDocument.insertString(messageDocument.getLength(), messageComps.get(0), TextAttributeCustom.getAttrTimestamp());
                messageDocument.insertString(messageDocument.getLength(), messageComps.get(1), TextAttributeCustom.getAttrUsername());
                messageDocument.insertString(messageDocument.getLength(), messageComps.get(2), TextAttributeCustom.getAttrMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void appendFileMessageToGUI(FileMessage fileMessage) {
         try {
            if (messageArea != null) {
                // [HH:mm] username sent file FILE
                ArrayList<String> messageComps = MessageUtil.getMessageStringComps(fileMessage);
                messageDocument.insertString(messageDocument.getLength(), messageComps.get(0), TextAttributeCustom.getAttrTimestamp());
                messageDocument.insertString(messageDocument.getLength(), messageComps.get(1), TextAttributeCustom.getAttrUsername());
                messageDocument.insertString(messageDocument.getLength(), " sent file", TextAttributeCustom.getAttrMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void appendTextUserEntered(String text) {
        try {
            if (messageArea != null) {
                messageDocument.insertString(messageDocument.getLength(), text + "\n", TextAttributeCustom.getAttrUserEntered());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void receiveMessage() throws IOException, InterruptedException, ClassNotFoundException {
        Message message = (Message) ois.readObject();
        appendMessageToGUI(message);
    }

    void receiveFileMessage() throws IOException, ClassNotFoundException {
        FileMessage fileMessage = (FileMessage) ois.readObject();
        appendFileMessageToGUI(fileMessage);
    }

    void sendMessage(Message message) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendMessage() {
        String messageBody = inputField.getText();
        inputField.setText("");

        Message message = new Message(ApplicationContext.getUser(), messageBody, new Date());

//        try {
//            oos.writeUTF(Constants.SEND_MESSAGE);
//            oos.flush();
//
//            oos.writeObject(message);
//            oos.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    void sendFile(File file){
        FileMessage fileMessage = new FileMessage(ApplicationContext.getUser(), "", new Date(), file);
//        try {
//            System.out.println(file.getAbsolutePath());
//
//            oos.writeUTF(Constants.SEND_FILE);
//            oos.flush();
//
//            oos.writeObject(fileMessage);
//            oos.flush();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    class sendFileAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            File chosenFile;
            if(fileChooser.showOpenDialog(Client.this) == JFileChooser.APPROVE_OPTION) {
                chosenFile = fileChooser.getSelectedFile();
                sendFile(chosenFile);
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new Client()).start();
//        SwingUtilities.invokeLater(() -> {
//            FlatLightLaf.setup();
//            new Client();
//        });
    }
}

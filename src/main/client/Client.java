package main.client;

import com.formdev.flatlaf.FlatLightLaf;
import main.model.Message;
import main.model.User;
import main.utils.Constants;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

public class Client extends JFrame implements Runnable {

    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;

    JPanel panel;
    JTextArea messageArea;
    JScrollPane messageAreaScroll;
    JTextField inputField;

    @Override
    public void run() {
        SwingUtilities.invokeLater(() -> {
            FlatLightLaf.setup();
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

            while(true) {
                while(ois.available() == 0) {
                    Thread.sleep(1);
                }

                String typeMessage = ois.readUTF();
                if(typeMessage.equals(Constants.SEND_MESSAGE)) {
                    receiveMessageFromServer();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void receiveMessageFromServer() throws IOException, InterruptedException, ClassNotFoundException {
        Message message = (Message) ois.readObject();
        appendMessageToGui(message);
    }

    void sendMessageToServer(Message message) {
        try {
            oos.writeUTF(Constants.SEND_MESSAGE);
            oos.flush();

            oos.writeObject(message);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createGUI() {
        panel = new JPanel(new MigLayout());
        messageArea = new JTextArea();
        messageAreaScroll = new JScrollPane(messageArea);
        inputField = new JTextField();

        // Message Area
        messageArea.setLineWrap(true);
        messageArea.setEditable(false);
        messageArea.setBackground(Color.white);
        messageAreaScroll.setPreferredSize(new Dimension(Constants.GUI_WIDTH, (int) (Constants.GUI_HEIGHT * 0.9)));

        // Input field
        inputField.setPreferredSize(new Dimension(Constants.GUI_WIDTH, (int) (Constants.GUI_HEIGHT * 0.1)));
        inputField.addKeyListener(new SendAction());

        panel.add(messageAreaScroll, "wrap");
        panel.add(inputField, "wrap");

        setContentPane(panel);

        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(Constants.GUI_WIDTH, Constants.GUI_HEIGHT);
        setLocationRelativeTo(null);
    }

    void appendMessageToGui(Message message) {
        if(messageArea != null) {
            messageArea.append(message.toString());
        }
    }

    class SendAction extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                String messageBody = inputField.getText();
                inputField.setText("");

                sendMessageToServer(new Message(new User("hoangtrung1801", "", ""), messageBody, new Date()));
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

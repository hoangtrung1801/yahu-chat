/*
 * Created by JFormDesigner on Tue Jul 05 21:31:49 ICT 2022
 */

package server;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.StyledDocument;

import model.User;
import net.miginfocom.swing.*;

/**
 * @author unknown
 */
public class ChatServerGUI extends JFrame {
    public static void main(String[] args) {
        new ChatServerGUI().setVisible(true);
    }

    public StatusListener getListener() {
        return listener;
    }

    public void setListener(StatusListener listener) {
        this.listener = listener;
    }

    public interface StatusListener {
        void start();
        void stop();
    }

    private boolean isStart = false;
    private StyledDocument document;
    private StatusListener listener;

    public ChatServerGUI() {
        initComponents();
        document = logPane.getStyledDocument();
    }

    public void log(String message) {
        try {
            document.insertString(document.getLength(), message + "\n", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void log(User user, String message) {
        try {
            document.insertString(document.getLength(), user.getUsername() + "> " +  message + "\n", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStart() {isStart = true; statusLabel.updateUI();}
    public void setStop() {isStart = false; statusLabel.updateUI();}

    private void startBtn(ActionEvent e) {
        setStart();
        listener.start();
    }

    private void stopBtn(ActionEvent e) {
        setStop();
        listener.stop();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        logScrollPane = new JScrollPane();
        logPane = new JTextPane();
        panel1 = new JPanel();
        startBtn = new JButton();
        stopBtn = new JButton();
        statusLabel = new StatusLabel();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        var contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[grow,fill]",
            // rows
            "[grow]" +
            "[]"));

        //======== logScrollPane ========
        {
            logScrollPane.setBorder(new CompoundBorder(
                new TitledBorder("Log"),
                new EmptyBorder(1, 0, 0, 0)));

            //---- logPane ----
            logPane.setBackground(Color.white);
            logPane.setEditable(false);
            logScrollPane.setViewportView(logPane);
        }
        contentPane.add(logScrollPane, "cell 0 0,growy");

        //======== panel1 ========
        {
            panel1.setLayout(new MigLayout(
                "insets 0,hidemode 3",
                // columns
                "[fill]" +
                "[fill]10" +
                "[fill]",
                // rows
                "[]"));

            //---- startBtn ----
            startBtn.setText("Start");
            startBtn.addActionListener(e -> startBtn(e));
            panel1.add(startBtn, "cell 0 0");

            //---- stopBtn ----
            stopBtn.setText("Stop");
            stopBtn.addActionListener(e -> stopBtn(e));
            panel1.add(stopBtn, "cell 1 0");
            panel1.add(statusLabel, "pad 0,cell 2 0,align center center,grow 0 0");
        }
        contentPane.add(panel1, "cell 0 1");
        setSize(500, 400);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        setTitle("Server");
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JScrollPane logScrollPane;
    private JTextPane logPane;
    private JPanel panel1;
    private JButton startBtn;
    private JButton stopBtn;
    private StatusLabel statusLabel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private class StatusLabel extends JLabel {
        private StatusLabel() {
            initComponents();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if(isStart) g.setColor(Color.green);
            else g.setColor(Color.red);

            g.fillOval(0, 0, 20, 20);
            ui.update(g, this);
        }

        private void initComponents() {
            // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
            setPreferredSize(new Dimension(20, 20));
            setMinimumSize(new Dimension(20, 20));
            // JFormDesigner - End of component initialization  //GEN-END:initComponents
        }

        // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
        // JFormDesigner - End of variables declaration  //GEN-END:variables
    }
}

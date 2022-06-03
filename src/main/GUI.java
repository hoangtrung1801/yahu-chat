package main;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class GUI extends JFrame {

    static int WIDTH = 500;
    static int HEIGHT = 500;
    ChatClient client;

    JPanel panel;

    JScrollPane scrollTableMessages;
    JTable tableMessages;
    DefaultTableModel tableMessagesModel;

    JList listMessages;
    DefaultListModel listMessagesModel;
    JScrollPane scrollListMessages;

    JPanel messageToolsPanel;
    JTextField input;

    String[] tableMessagesColumn = {"Name", "Message"};

    public GUI(ChatClient client) {
        this.client = client;
        panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0,5, 0));

        createTableMessages();
        createInputMessage();
//        createListMessages();

        panel.add(scrollTableMessages, BorderLayout.CENTER);
        panel.add(messageToolsPanel, BorderLayout.SOUTH);

        setContentPane(panel);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    void createInputMessage() {
        // Message tool
        messageToolsPanel = new JPanel();
        messageToolsPanel.setBorder(new EmptyBorder(0, 2, 0, 2));

        input = new JTextField();
        input.addKeyListener(new SendAction());
        input.setPreferredSize(new Dimension(WIDTH - 20, 40));

        messageToolsPanel.add(input);
    }

    void createTableMessages() {
        // Table messages
        tableMessagesModel = new DefaultTableModel(tableMessagesColumn, 0);
        tableMessages = new JTable(tableMessagesModel);
        tableMessages.setFillsViewportHeight(true);
        tableMessages.setTableHeader(null);
        tableMessages.setShowVerticalLines(false);
        tableMessages.setRowHeight(60);
        tableMessages.setRowSelectionAllowed(false);

        tableMessages.getColumnModel().getColumn(0).setCellRenderer(new MessageHeaderCellRender());
        tableMessages.getColumnModel().getColumn(1).setCellRenderer(new MessageBodyCellRender());
        tableMessages.getColumnModel().getColumn(0).setMaxWidth(50);

        scrollTableMessages = new JScrollPane(tableMessages);
    }

    public void showMessageReceived(String message) {
        tableMessagesModel.addRow(new String[] {"Client", message});
    }

    class SendAction extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                try {
                    client.sendMessage(input.getText());
                    input.setText("");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatLightLaf.setup();
            new GUI(null);
        });
    }
}

class MessageBodyCellRender extends JTextArea implements TableCellRenderer {
    public MessageBodyCellRender() {
        setLineWrap(true);
        setWrapStyleWord(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText(value.toString());
        setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
        if(table.getRowHeight(row) != getPreferredSize().height) {
            table.setRowHeight(row, getPreferredSize().height);
        }
        return this;
    }
}

class MessageHeaderCellRender extends DefaultTableCellRenderer {
    public MessageHeaderCellRender() {
        setHorizontalAlignment(DefaultTableCellRenderer.LEFT);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JTextArea textArea = new JTextArea();
        textArea.setText(value.toString());

        return textArea;
    }
}
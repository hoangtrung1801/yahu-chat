import client.components.ConversationTab;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import dao.ConversationDAO;
import dao.implement.ConversationDAOImpl;
import dto.ConversationDto;
import dto.MessageDto;
import model.Conversation;
import org.modelmapper.ModelMapper;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Test {
    public Test() throws Exception {
        run();
    }

    public void run() throws Exception {
        JFrame frame = new JFrame();

        ConversationTab tab = new ConversationTab();
        JLabel label = new JLabel("File" );
        label.setAlignmentY(0.85f);
        tab.getMessagePane().insertComponent(label);
        tab.getMessagePane().getStyledDocument().insertString(
                tab.getMessagePane().getStyledDocument().getLength(),
                "message",
                null
        );

        FileLabel label1 = new FileLabel("hello");
        tab.getMessagePane().insertComponent(label1);

        frame.add(tab);

        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        new Test();
    }
    private class  FileLabel extends JLabel {
        public FileLabel() {}

        public FileLabel(String text) {
            super("<HTML><u style=\"font-style: italic; color: #808080\">" + text + "</u></HTML>");
            setAlignmentY(0.85f);
        }
    }

    private class FilePanel extends JPanel {
        public FilePanel() {
            super();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.red);
            g2.drawString("hello world", 0, 0);
        }
    }
}

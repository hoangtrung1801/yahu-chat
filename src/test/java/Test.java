import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import dao.ConversationDAO;
import dao.implement.ConversationDAOImpl;
import dto.ConversationDto;
import model.Conversation;
import org.modelmapper.ModelMapper;

import javax.swing.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Test {
    public Test() throws Exception {
        run();
    }

    public void run() throws Exception {

        ModelMapper modelMapper = new ModelMapper();

        ConversationDAO conversationDAO = new ConversationDAOImpl();
        Conversation conversation = conversationDAO.readById(1);

        ConversationDto conversationDto = modelMapper.map(conversation, ConversationDto.class);
        System.out.println(conversationDto);
    }

    public static void main(String[] args) throws Exception {
        new Test();
    }
}

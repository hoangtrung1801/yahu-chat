package client;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import dto.ConversationDto;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class VideoCallController {

    public VideoCallGUI gui;

    protected int FPS = 30;
    protected ScheduledExecutorService timeWorker;
    protected ScheduledFuture<?> videoCallFuture;
    private Webcam webcam;
    protected ConversationDto conversation;

    public VideoCallController(VideoCallGUI gui, ConversationDto conversation) {
        this.gui = gui;
        this.timeWorker = new ScheduledThreadPoolExecutor(1);
        this.conversation = conversation;

        // ONLY FOR TEST
        if(ChatClient.user.getUsername().equals("user1")) return;

        Webcam.setAutoOpenMode(true);
        webcam = Webcam.getDefault();
        if(!webcam.isOpen()) {
            webcam.setViewSize(WebcamResolution.VGA.getSize());
            webcam.open();
        }
    }

    void startCall() {
        Runnable videoCallTask = new VideoCallTask();
        videoCallFuture = timeWorker.scheduleWithFixedDelay(
                videoCallTask,
                0,
                1000/FPS,
                TimeUnit.MILLISECONDS
        );
    }

    private class VideoCallTask implements Runnable {
        @Override
        public void run() {
            System.out.println("send video call ");
            // ONLY FOR TEST
            if(ChatClient.user.getUsername().equals("user1")) {
                try {
                    BufferedImage image = ImageIO.read(getClass().getResource("/assets/onthemoon.png"));
                    ChatClient.connection.sendVideoCallData(conversation, image);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(!webcam.isOpen()) return;
            BufferedImage image = webcam.getImage();
            ChatClient.connection.sendVideoCallData(conversation, image);

//            gui.videoPannel.updateImage(image);
        }
    }
}

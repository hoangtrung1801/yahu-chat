import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

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
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());

        WebcamPanel panel = new WebcamPanel(webcam);
//        panel.setFPSDisplayed(true);
//        panel.setDisplayDebugInfo(true);
//        panel.setImageSizeDisplayed(true);
        panel.setMirrored(true);

        JFrame frame = new JFrame();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

//        Webcam webcam1 = Webcam.getDefault();
//        WebcamPanel panel1 = new WebcamPanel(webcam);
//        panel1.setMirrored(true);
//
//        JFrame frame1 = new JFrame();
//        frame1.add(panel1);
//        frame1.pack();
//        frame1.setVisible(true);
//        frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame1.setLocationRelativeTo(null);

//        Runnable task = new Task();
//        ScheduledExecutorService timeWorker = new ScheduledThreadPoolExecutor(1);
//        ScheduledFuture<?> scheduledFuture = timeWorker.scheduleWithFixedDelay(
//                task,
//                0,
//                1,
//                TimeUnit.SECONDS
//        );
    }

    private class Task implements Runnable {
        @Override
        public void run() {
            System.out.println("running...");
        }
    }

    public static void main(String[] args) throws Exception {
        new Test();
    }
}

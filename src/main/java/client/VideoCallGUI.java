package client;

import client.components.VideoPanel;
import com.github.sarxos.webcam.WebcamResolution;
import dto.ConversationDto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class VideoCallGUI extends JFrame {
    protected final Dimension dimension = WebcamResolution.VGA.getSize();
    protected final VideoPanel videoPannel;
	protected final JFrame window;

	public final VideoCallController controller;

	public VideoCallGUI(String name, ConversationDto conversation) {
		super();
		this.controller = new VideoCallController(this, conversation);
		this.window = new JFrame(name);
		this.videoPannel = new VideoPanel();

		this.window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				System.out.println("Close window");
				close();
			}
		});

		this.videoPannel.setPreferredSize(dimension);
		this.window.add(videoPannel);
		this.window.pack();
		this.window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void setVisible(boolean visible) {
		this.window.setVisible(visible);
	}

	public void updateImage(BufferedImage image) {
		videoPannel.updateImage(image);
	}

	public void close(){
		window.dispose();
		videoPannel.close();
		controller.closeCall();
	}
}

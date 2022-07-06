package client;

import client.components.VideoPanel;
import com.github.sarxos.webcam.WebcamResolution;
import dto.ConversationDto;

import javax.swing.*;
import java.awt.*;
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

		this.videoPannel.setPreferredSize(dimension);
		this.window.add(videoPannel);
		this.window.pack();
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
	}
}

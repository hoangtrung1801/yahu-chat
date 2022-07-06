package client.components;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class VideoPanel extends JPanel {
    public static void main(String[] args) {
        JFrame frame = new JFrame();

        VideoPanel panel = new VideoPanel();
        panel.setPreferredSize(new Dimension(500, 500));

        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Runnable task = new Runnable() {
			@Override
			public void run() {
				BufferedImage image = null;
				System.out.println("update image");
				try {
					image = ImageIO.read(getClass().getResource("/assets/onthemoon.png"));
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				panel.updateImage(image);
			}
		};
		ScheduledExecutorService timeWorker = new ScheduledThreadPoolExecutor(1);
        ScheduledFuture<?> scheduledFuture = timeWorker.scheduleWithFixedDelay(
                task,
                0,
                1,
                TimeUnit.SECONDS
        );
    }

	protected BufferedImage image;
	protected final ExecutorService worker = Executors.newSingleThreadExecutor();

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (image == null) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setBackground(Color.BLACK);
			g2.fillRect(0, 0, getWidth(), getHeight());

			int cx = (getWidth() - 70) / 2;
			int cy = (getHeight() - 40) / 2;

			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.LIGHT_GRAY);
			g2.fillRoundRect(cx, cy, 70, 40, 10, 10);
			g2.setColor(Color.WHITE);
			g2.fillOval(cx + 5, cy + 5, 30, 30);
			g2.setColor(Color.LIGHT_GRAY);
			g2.fillOval(cx + 10, cy + 10, 20, 20);
			g2.setColor(Color.WHITE);
			g2.fillOval(cx + 12, cy + 12, 16, 16);
			g2.fillRoundRect(cx + 50, cy + 5, 15, 10, 5, 5);
			g2.fillRect(cx + 63, cy + 25, 7, 2);
			g2.fillRect(cx + 63, cy + 28, 7, 2);
			g2.fillRect(cx + 63, cy + 31, 7, 2);

			g2.setColor(Color.DARK_GRAY);
			g2.setStroke(new BasicStroke(3));
			g2.drawLine(0, 0, getWidth(), getHeight());
			g2.drawLine(0, getHeight(), getWidth(), 0);

			String str = image == null ? "Connecting To Server" : "No Image";
			FontMetrics metrics = g2.getFontMetrics(getFont());
			int w = metrics.stringWidth(str);
			int h = metrics.getHeight();

			g2.setColor(Color.WHITE);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			g2.drawString(str, (getWidth() - w) / 2, cy - h);
			// w = metrics.stringWidth(str);
			// h = metrics.getHeight();
			// g2.drawString(str, (getWidth() - w) / 2, cy - 2 * h);

		} else {
			// owner.getGraphics().drawImage(image, 0, 0, image.getWidth(),
			// image.getHeight(), null);
			// g2.clearRect(0, 0, image.getWidth(), image.getHeight());
			int width = image.getWidth();
			int height = image.getHeight();
			g2.clearRect(0, 0, width, height);
			g2.drawImage(image, 0, 0, width, height, null);
			// setBounds(getBounds().x , getBounds().y, image.getWidth(),
			// image.getHeight());
			setSize(width, height);
		}
	}

	public void updateImage(final BufferedImage update) {
		worker.execute(new Runnable() {

			@Override
			public void run() {
				// image = scaleUPMaker.make(update);
//				System.out.println("get image");
				image = update;
				repaint();
			}
		});
	}

	public void close() {
		worker.shutdown();
	}
}

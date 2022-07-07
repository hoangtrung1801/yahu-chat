package client.components;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class FileChooserImage extends JFileChooser {
    JLabel img;
    String[] extensions = {"jpg", "png", "gif", "jpeg"};

    public FileChooserImage() {
        super();

        img = new JLabel();
        img.setPreferredSize(new Dimension(180, 180));
        this.setAccessory(img);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("image extension", extensions);
        this.setFileFilter(filter);

        this.addPropertyChangeListener(evt -> {
            SwingWorker<Image, Void> worker = new SwingWorker<>() {
                @Override
                protected Image doInBackground() throws Exception {
                    if (evt.getPropertyName().equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
                        File f = getSelectedFile();
                        if(f.isDirectory()) return null;
                        BufferedImage bim = ImageIO.read(f);

                        return Scalr.resize(bim, 180);
                    }
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        Image i = get(1L, TimeUnit.NANOSECONDS);
                        if (i == null) return;
                        img.setIcon(new ImageIcon(i));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            };
            worker.execute();
        });
    }
}

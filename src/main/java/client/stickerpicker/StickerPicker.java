/*
 * Created by JFormDesigner on Thu Jul 07 23:53:50 ICT 2022
 */

package client.stickerpicker;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.*;
import net.miginfocom.swing.*;

/**
 * @author unknown
 */
public class StickerPicker extends JFrame {
    public static void main(String[] args) {
        StickerPicker stickerPicker = new StickerPicker();

        stickerPicker.setVisible(true);
    }

    
    private static final Collection<Sticker> allStickers = Arrays.stream(Objects.requireNonNull(new File("D:\\Development\\Java\\ClassOOP\\ChatRealtime\\src\\main\\resources\\stickers")
                    .listFiles()))
            .map(file -> {
                try {
                    Sticker sticker = new Sticker(file.getName(), new FileInputStream(file).readAllBytes());
                    return sticker;
                } catch (Exception e ) {
                    e.printStackTrace();
                }
                return null;
            }).toList();



    public interface StickerPickerListener {
        public void userSelectedSticker(Sticker sticker);
    }

    private StickerPickerListener listener;

    public StickerPicker() {
        initComponents();

        for(var sticker: allStickers) {
            StickerItem item = new StickerItem(sticker, s -> listener.userSelectedSticker(s));
            stickerTable.add(item, "width 64, height 64");
        }
    }

    public StickerPickerListener getListener() {
        return listener;
    }

    public void setListener(StickerPickerListener listener) {
        this.listener = listener;
    }
    
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        scrollPane1 = new JScrollPane();
        stickerTable = new JPanel();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "insets 0,hidemode 3",
            // columns
            "[grow,fill]",
            // rows
            "[grow]"));

        //======== scrollPane1 ========
        {

            //======== stickerTable ========
            {
                stickerTable.setLayout(new MigLayout(
                    "hidemode 3,gap 10 10,wrap 5",
                    // columns
                    "[]",
                    // rows
                    "[]"));
            }
            scrollPane1.setViewportView(stickerTable);
        }
        contentPane.add(scrollPane1, "cell 0 0,grow");
        setSize(400, 400);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JScrollPane scrollPane1;
    private JPanel stickerTable;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public static class StickerItem extends JPanel {
        protected interface StickerItemListener {
            public void userSelectedSticker(Sticker sticker);
        }
        
        private Sticker sticker;
        private StickerItemListener listener;
        
        private StickerItem() {
            initComponents();
        }
        
        private StickerItem(Sticker sticker, StickerItemListener listener) {
            this.sticker = sticker;
            this.listener = listener;
            initComponents();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            try {
                InputStream is = new ByteArrayInputStream(sticker.getStickerImage());
                BufferedImage image = ImageIO.read(is);
                
                g2.drawImage(image, 0,0, 64, 64, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void clickSticker() {
            listener.userSelectedSticker(sticker);
        }

        private void initComponents() {
            // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents

            //======== this ========
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    clickSticker();
                }
            });
            setLayout(null);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < getComponentCount(); i++) {
                    Rectangle bounds = getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                setMinimumSize(preferredSize);
                setPreferredSize(preferredSize);
            }
            // JFormDesigner - End of component initialization  //GEN-END:initComponents
        }

        // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
        // JFormDesigner - End of variables declaration  //GEN-END:variables


        public Sticker getSticker() {
            return sticker;
        }

        public void setSticker(Sticker sticker) {
            this.sticker = sticker;
            repaint();
        }
    }
}

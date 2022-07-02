package client.emojipicker;

import com.vdurmont.emoji.Emoji;

import javax.swing.*;
import java.awt.*;

public class EmojiPicker extends JFrame {
    public interface EmojiSelectListener {
        public void userSelectEmoji(Emoji emoji);
    }

    private Emoji selectedEmoji;
    private EmojiSelectListener listener;

    public EmojiPicker() {
        EmojiTable table = new EmojiTable(8, new Font("OpenMoji", Font.PLAIN, 34), true);
        table.setRowHeight(34);

        table.setClickListener(emoji -> listener.userSelectEmoji(emoji));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setViewportView(table);

        add(scrollPane);

        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        setName("Emoji Picker");
    }

    public Emoji getSelectedEmoji() {
        return selectedEmoji;
    }

    public void setSelectedEmoji(Emoji selectedEmoji) {
        this.selectedEmoji = selectedEmoji;
    }

    public void setClickListener(EmojiSelectListener listener) {
        this.listener = listener;
    }
}

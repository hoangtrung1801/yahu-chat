package utility;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class MessageAttributeSet {

    public static SimpleAttributeSet getAttrForTimestamp() {
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setForeground(attr, Color.lightGray);
        return attr;
    }

    public static SimpleAttributeSet getAttrForUsername() {
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setBold(attr, true);
        StyleConstants.setForeground(attr, new Color(0xA4A4A4));

        return attr;
    }

    public static SimpleAttributeSet getAttrForTargetUsername() {
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setBold(attr, true);
        StyleConstants.setForeground(attr, new Color(0x3817C9));

        return attr;
    }

    public static SimpleAttributeSet getAttrForMessageText() {
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setBold(attr, false);
        StyleConstants.setForeground(attr, new Color(0x000000));

        return attr;
    }
}


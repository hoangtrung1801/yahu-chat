package utility;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class TextAttributeCustom {

    public static SimpleAttributeSet getAttrTimestamp() {
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setForeground(attr, Color.lightGray);
        return attr;
    }

    public static SimpleAttributeSet getAttrUsername() {
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setBold(attr, true);
        StyleConstants.setForeground(attr, new Color(0x03A9F4));

        return attr;
    }

    public static SimpleAttributeSet getAttrMessage() {
        SimpleAttributeSet attr = new SimpleAttributeSet();
//        StyleConstants.setForeground(attr, Color.darkGray);

        return attr;
    }

    public static SimpleAttributeSet getAttrUserEntered() {
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setBold(attr, true);
        StyleConstants.setForeground(attr, new Color(0x4CAF50));

        return attr;
    }
}


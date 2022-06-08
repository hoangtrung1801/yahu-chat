package main.utilities;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class TextAttributeCustom {

    public static SimpleAttributeSet getAttrTimestamp() {
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setForeground(attr, Color.orange);

        return attr;
    }

    public static SimpleAttributeSet getAttrUsername() {
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setBold(attr, true);
        StyleConstants.setForeground(attr, Color.PINK);

        return attr;
    }

    public static SimpleAttributeSet getAttrMessage() {
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setForeground(attr, Color.darkGray);

        return attr;
    }

    public static SimpleAttributeSet getAttrUserEntered() {
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setBold(attr, true);
        StyleConstants.setForeground(attr, Color.GREEN);

        return attr;
    }
}


package client.emojipicker;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Load the embedded font "OpenMoji.ttf"
 *
 * @author locutus
 */
public class OpenMojiFont {

    private static final String openMojiFont = "/fonts/OpenMoji.ttf";

    public static void load() throws IOException, FontFormatException {

        //Create a temp file to copy font from package
        File fontFile = File.createTempFile("openMojiFont", "ttf");
        fontFile.deleteOnExit();

        InputStream input;
        FileOutputStream output = null;
        input = OpenMojiFont.class.getResourceAsStream(openMojiFont);
        if (input == null)
            throw new IOException("Cannot find file in resource path: " + openMojiFont);

        int read;
        byte[] buffer = new byte[4096];
        try {
            output = new FileOutputStream(fontFile);
            while((read = input.read(buffer)) != -1){
                output.write(buffer, 0, read);
            }

        }
        finally {
            input.close();
            if (output != null)
                 output.close();

        }

        //load the font
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, fontFile));

    }


}